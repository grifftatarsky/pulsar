package com.gpt.pulsarconsumer.sync;


import com.gpt.pulsarconsumer.model.StationUpsert;
import com.gpt.pulsarconsumer.repo.StationRepository;
import com.gpt.pulsarconsumer.service.MetricsService;
import com.gpt.pulsarconsumer.ws.MetricsWebSocketHandler;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.pulsar.config.PulsarListenerEndpointRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReconcileService
{

    private final MetricsService metrics;
    private final MetricsWebSocketHandler ws;

    private final SnapshotApplierService applier;

    private final StationRepository stationRepository;

    private final ObjectProvider<PulsarListenerEndpointRegistry> registryProvider;

    public void reconcileThenStartTailing()
    {
        Map<String, StationUpsert> snapshot = applier.readCompactedTopicSnapshot();

        applier.applySnapshotTransactional(snapshot);
        metrics.setCountsFromDb(stationRepository);
        ws.broadcastSnapshot();

        PulsarListenerEndpointRegistry registry = registryProvider.getIfAvailable();

        if (registry != null)
        {
            var container = registry.getListenerContainer("stationChangelog");
            
            if (container != null)
            {
                container.start();
            }
        }
    }
}