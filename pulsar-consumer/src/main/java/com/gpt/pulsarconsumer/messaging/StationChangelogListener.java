package com.gpt.pulsarconsumer.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpt.pulsarconsumer.domain.Station;
import com.gpt.pulsarconsumer.domain.StationType;
import com.gpt.pulsarconsumer.model.StationUpsert;
import com.gpt.pulsarconsumer.repo.StationRepository;
import com.gpt.pulsarconsumer.service.MetricsService;
import com.gpt.pulsarconsumer.ws.MetricsWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.pulsar.client.api.SubscriptionType.Key_Shared;

@Component
@RequiredArgsConstructor
public class StationChangelogListener
{

    private final StationRepository repo;
    private final MetricsService metrics;
    private final ObjectMapper objectMapper;
    private final MetricsWebSocketHandler broadcaster;

    @PulsarListener(
        id = "stationChangelog",
        autoStartup = "false",
        topics = "${app.pulsar.topic}",
        subscriptionName = "${app.pulsar.subscription}",
        subscriptionType = Key_Shared
    )
    @Transactional
    public void onMessage(@Payload String payload)
    {
        if (payload == null || payload.isBlank())
        {
            return;
        }

        try
        {
            StationUpsert u = objectMapper.readValue(payload, StationUpsert.class);

            if (u.getStationId() == null || u.getStationId().isBlank())
            {
                return;
            }

            if (u.isDeleted())
            {
                repo.findByStationId(u.getStationId()).ifPresent(s -> repo.deleteById(s.getId()));
            }
            else
            {
                StationType t = parseType(u.getType());

                repo.findByStationId(u.getStationId()).map(existing -> {
                    if (u.getVersion() > existing.getVersion())
                    {
                        existing.setType(t);
                        existing.setVersion(u.getVersion());
                        existing.setUpdatedAt(java.time.Instant.ofEpochMilli(u.getUpdatedAt()));
                        return repo.save(existing);
                    }
                    else
                    {
                        return existing;
                    }
                }).orElseGet(() -> repo.save(
                    new Station(
                        u.getStationId(),
                        t,
                        u.getVersion(),
                        java.time.Instant.ofEpochMilli(u.getUpdatedAt())
                    )));
            }
            // Recompute counts for correctness (simple and fine for demo), then broadcast.
            metrics.setCountsFromDb(repo);
            broadcaster.broadcastSnapshot();
        }
        catch (Exception ignored)
        {
        }
    }

    private StationType parseType(String s)
    {
        if (s == null)
        {
            return StationType.A;
        }

        String v = s.trim().toUpperCase();

        return switch (v)
        {
            case "A" -> StationType.A;
            case "B" -> StationType.B;
            case "C" -> StationType.C;
            default -> StationType.UNKNOWN;
        };
    }
}
