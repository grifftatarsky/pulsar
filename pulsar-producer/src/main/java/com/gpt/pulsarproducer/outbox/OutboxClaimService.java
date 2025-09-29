package com.gpt.pulsarproducer.outbox;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxClaimService
{
    private final OutboxRepository repo;

    @Transactional
    public List<OutboxEvent> claimBatch(int size)
    {
        return repo.claimAndMark(size);
    }
}