package com.gpt.pulsarproducer.outbox;


import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class OutboxPublisher
{

    private final OutboxRepository repo;
    private final Producer<String> producer; // bound to stations.changelog

    /** Flush small batches frequently, transactionally so status updates persist. */
    @Scheduled(fixedDelay = 250)
    @Transactional
    public void flushOutbox()
    {
        List<OutboxEvent> batch = repo.findTop50ByStatusOrderByIdAsc("NEW");
        for (OutboxEvent e : batch)
        {
            try
            {
                // key = stationId, value = JSON payload
                producer.newMessage()
                        .key(e.getMessageKey())
                        .value(e.getPayload())
                        .send(); // TODO: does this guy scale? Batch?

                e.setStatus("SENT");
                e.setSentAt(Instant.now());
            }
            catch (PulsarClientException ex)
            {
                e.setStatus("FAILED"); // TODO: add retry/backoff later
            }
        }
    }
}
