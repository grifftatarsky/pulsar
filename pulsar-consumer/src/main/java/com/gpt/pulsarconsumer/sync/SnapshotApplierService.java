package com.gpt.pulsarconsumer.sync;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpt.pulsarconsumer.config.PulsarProperties;
import com.gpt.pulsarconsumer.domain.Station;
import com.gpt.pulsarconsumer.domain.StationType;
import com.gpt.pulsarconsumer.model.StationUpsert;
import com.gpt.pulsarconsumer.repo.StationRepository;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Reader;
import org.apache.pulsar.client.api.Schema;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SnapshotApplierService
{
    private final StationRepository repo;

    private final PulsarClient pulsarClient;
    private final PulsarProperties pulsarProperties;

    private final ObjectMapper objectMapper;

    @Transactional
    public void applySnapshotTransactional(Map<String, StationUpsert> latest)
    {
        for (Map.Entry<String, StationUpsert> e : latest.entrySet())
        {
            StationUpsert u = e.getValue();
            String stationId = e.getKey();

            if (u.isDeleted())
            {
                repo.findByStationId(stationId).ifPresent(s -> repo.deleteById(s.getId()));
                continue;
            }

            StationType type = StationType.valueOf(u.getType());
            Instant updatedAt = Instant.ofEpochMilli(u.getUpdatedAt());
            long version = u.getVersion();

            Optional<Station> existingOpt = repo.findByStationId(u.getStationId());
            if (existingOpt.isPresent())
            {
                Station cur = existingOpt.get();
                if (version > cur.getVersion())
                {
                    cur.setType(type);
                    cur.setVersion(version);
                    cur.setUpdatedAt(updatedAt);
                    repo.save(cur);
                }
            }
            else
            {
                repo.save(new Station(u.getStationId(), type, version, updatedAt));
            }
        }
    }

    /**
     * Reads the compacted topic (no transaction; IO + CPU only).
     */
    Map<String, StationUpsert> readCompactedTopicSnapshot()
    {
        Map<String, StationUpsert> lastById = new HashMap<>();

        try (Reader<String> reader =
                 pulsarClient.newReader(Schema.STRING)
                             .topic(pulsarProperties.getTopic())
                             .startMessageId(MessageId.earliest)
                             .readCompacted(true)
                             .create())
        {

            while (true)
            {
                Message<String> msg = reader.readNext(300, TimeUnit.MILLISECONDS);
                if (msg == null)
                {
                    break;
                }

                String key = msg.getKey();
                if (key == null)
                {
                    // no key? skip; compacted topics are key-based
                    continue;
                }

                String value = msg.getValue();
                if (value == null)
                {
                    // Treat null payload as tombstone (delete)
                    StationUpsert tomb = new StationUpsert();
                    tomb.setStationId(key);
                    tomb.setDeleted(true);
                    lastById.put(key, tomb);
                    continue;
                }

                try
                {
                    StationUpsert upsert = objectMapper.readValue(value, StationUpsert.class);
                    lastById.put(key, upsert);
                }
                catch (Exception parseIgnored)
                {
                    // consider logging a structured warning here
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to read compacted topic snapshot", e);
        }

        return lastById;
    }
}