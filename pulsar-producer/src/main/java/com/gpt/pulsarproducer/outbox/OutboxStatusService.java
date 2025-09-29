package com.gpt.pulsarproducer.outbox;


import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxStatusService
{
    private final OutboxRepository repo;

    @Transactional
    public void markSentBulk(List<Long> ids)
    {
        if (!ids.isEmpty()) repo.markSentBulk(ids, Instant.now());
    }

    @Transactional
    public void markFailedBulk(List<Long> ids)
    {
        if (!ids.isEmpty()) repo.markFailedBulk(ids);
    }

    @Transactional
    public void markDlqBulk(List<Long> ids)
    {
        if (!ids.isEmpty()) repo.markDlqBulk(ids);
    }
}