package com.gpt.pulsarproducer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpt.pulsarproducer.config.PulsarProperties;
import com.gpt.pulsarproducer.domain.Station;
import com.gpt.pulsarproducer.domain.StationType;
import com.gpt.pulsarproducer.model.StationUpsert;
import com.gpt.pulsarproducer.outbox.OutboxEvent;
import com.gpt.pulsarproducer.outbox.OutboxRepository;
import com.gpt.pulsarproducer.repo.StationRepository;
import com.gpt.pulsarproducer.ws.MetricsWebSocketHandler;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StationService
{

    private final StationRepository stations;
    private final OutboxRepository outbox;
    private final ObjectMapper objectMapper;
    private final PulsarProperties pulsarProperties;
    private final MetricsWebSocketHandler ws;

    private final Random random = new Random();

    @Transactional
    public void addRandom()
    {
        StationType t = randomTypeDiff(null);

        Station s = stations.save(new Station(t));

        enqueueUpsert(s, false);

        ws.broadcastCounts(countsView());
    }

    @Transactional
    public void deleteRandom()
    {
        Station s = stations.pickRandom();

        if (s == null)
        {
            return;
        }

        stations.deleteById(Objects.requireNonNull(s.getId()));

        enqueueUpsert(s, true);

        ws.broadcastCounts(countsView());
    }

    @Transactional
    public void updateRandom()
    {
        Station s = stations.pickRandom();

        if (s == null)
        {
            return;
        }

        StationType next = randomTypeDiff(s.getType());

        s.setType(next);
        s.setVersion(s.getVersion() + 1L);
        s.setUpdatedAt(Instant.now());

        stations.save(s);

        enqueueUpsert(s, false);

        ws.broadcastCounts(countsView());
    }

    public Map<String, Long> countsView()
    {
        Map<String, Long> m = new HashMap<>();

        for (StationType t : StationType.values())
        {
            m.put(t.name(), stations.countByType(t));
        }

        return m;
    }

    private void enqueueUpsert(
        Station s,
        boolean deleted
    )
    {
        StationUpsert up = new StationUpsert(
            s.getStationId(),
            s.getType().name(),
            s.getVersion(),
            s.getUpdatedAt().toEpochMilli(),
            deleted
        );

        try
        {
            String json = objectMapper.writeValueAsString(up);

            OutboxEvent evt = new OutboxEvent(pulsarProperties.getTopic(), s.getStationId(), json);
            outbox.save(evt);
        }
        catch (JsonProcessingException ignored)
        {
            // TODO: This probably should not be this?
        }
    }

    private StationType randomTypeDiff(StationType current)
    {
        int r = random.nextInt(3);

        StationType pick = (r == 0 ? StationType.A : r == 1 ? StationType.B : StationType.C);

        if (current == null || pick != current)
        {
            return pick;
        }

        return (current == StationType.A) ? StationType.B : StationType.A;
    }
}
