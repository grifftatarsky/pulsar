package com.gpt.pulsarconsumer.sync;


import com.gpt.pulsarconsumer.model.StationUpsert;
import com.gpt.pulsarconsumer.repo.StationRepository;
import com.gpt.pulsarconsumer.service.MetricsService;
import com.gpt.pulsarconsumer.ws.MetricsWebSocketHandler;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncBootstrapService
{
    // CAN BE MADE COMMON

    private final StationRepository stationRepository;

    private final SnapshotApplierService applier;

    private final MetricsService metricsService;
    private final MetricsWebSocketHandler broadcaster;

    /**
     * Entry point invoked by ApplicationRunner (proxy boundary OK). Only triggers a rebuild if the DB is empty.
     */
    public void rebuildFromCompactedOnStart()
    {
        //
        if (stationRepository.count() > 0L)
        {
            // Already have state; skip full rebuild.
            return;
        }

        Map<String, StationUpsert> lastById = applier.readCompactedTopicSnapshot();
        applier.applySnapshotTransactional(lastById);

        metricsService.setCountsFromDb(stationRepository);
        broadcaster.broadcastSnapshot();
    }
}