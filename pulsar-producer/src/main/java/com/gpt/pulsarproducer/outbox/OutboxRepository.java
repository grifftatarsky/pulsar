package com.gpt.pulsarproducer.outbox;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long>
{
    List<OutboxEvent> findTop50ByStatusOrderByIdAsc(String status);
}
