package com.gpt.pulsarproducer.outbox;


import com.gpt.pulsarproducer.config.PulsarConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher
{
    private static final int BATCH_SIZE = 2_000;          // bump size
    private static final int MAX_BATCHES_PER_TICK = 50;
    private static final int MAX_ATTEMPTS = 10;

    private final OutboxClaimService claimService;
    private final OutboxStatusService statusService;
    private final PulsarConfig pulsarConfig;

    private final Producer<String> equipmentProducer;
    private final Producer<String> equipmentIdentifierProducer;
    private final Producer<String> equipmentTypeIdentifierProducer;

    @Scheduled(fixedDelay = 50)
    public void flushOutbox()
    {
        int loops = 0;
        while (loops++ < MAX_BATCHES_PER_TICK)
        {
            List<OutboxEvent> batch = claimService.claimBatch(BATCH_SIZE);
            if (batch.isEmpty()) break;

            // Thread-safe bags for results
            var sentIds = new ConcurrentLinkedQueue<Long>();
            var failIds = new ConcurrentLinkedQueue<Long>();
            var dlqIds = new ConcurrentLinkedQueue<Long>();

            // Pre-map producers once
            Map<String, Producer<String>> producers = Map.of(
                pulsarConfig.getEquipmentTopic(), equipmentProducer,
                pulsarConfig.getEquipmentIdentifierTopic(), equipmentIdentifierProducer,
                pulsarConfig.getEquipmentTypeIdentifierTopic(), equipmentTypeIdentifierProducer
            );

            try (var scope = new StructuredTaskScope.ShutdownOnFailure())
            {
                for (OutboxEvent e : batch)
                {
                    scope.fork(() -> {
                        Producer<String> p = producers.get(e.getTopic());
                        if (p == null)
                        {
                            // Treat as DLQ: unknown routing
                            dlqIds.add(e.getId());
                            return null;
                        }

                        try
                        {
                            // Blocking send is fine on VTs; lets Pulsar back-pressure naturally.
                            p.newMessage().key(e.getMessageKey()).value(e.getPayload()).send();
                            sentIds.add(e.getId());
                        }
                        catch (Exception ex)
                        {
                            int nextAttempts = e.getAttempts() + 1;
                            if (nextAttempts >= MAX_ATTEMPTS)
                            {
                                dlqIds.add(e.getId());
                            }
                            else
                            {
                                failIds.add(e.getId());
                            }
                        }
                        return null;
                    });
                }
                scope.join(); // propagate failures; bounds per-batch
            }
            catch (InterruptedException ie)
            {
                Thread.currentThread().interrupt();
                return;
            }

            // Single round of bulk state transitions per outcome
            if (!sentIds.isEmpty()) statusService.markSentBulk(toList(sentIds));
            if (!failIds.isEmpty()) statusService.markFailedBulk(toList(failIds)); // SQL computes backoff+jitter
            if (!dlqIds.isEmpty()) statusService.markDlqBulk(toList(dlqIds));
        }
    }

    private static List<Long> toList(ConcurrentLinkedQueue<Long> q)
    {
        return new ArrayList<>(q); // snapshot
    }
}