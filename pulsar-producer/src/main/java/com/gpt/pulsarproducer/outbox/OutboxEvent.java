package com.gpt.pulsarproducer.outbox;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox_event")
public class OutboxEvent
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String messageKey; // stationId

    @Lob
    @Column(nullable = false)
    private String payload; // JSON

    @Column(nullable = false)
    private String status; // NEW, SENT, FAILED

    @Column(nullable = false)
    private Instant createdAt;

    private Instant sentAt;

    public OutboxEvent(String topic, String messageKey, String payload)
    {
        this.topic = topic;
        this.messageKey = messageKey;
        this.payload = payload;
        this.status = "NEW";
        this.createdAt = Instant.now();
    }
}
