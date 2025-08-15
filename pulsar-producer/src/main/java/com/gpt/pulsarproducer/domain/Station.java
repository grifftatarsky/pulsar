package com.gpt.pulsarproducer.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "station")
public class Station
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_id", nullable = false, unique = true, updatable = false, length = 36)
    private String stationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private StationType type;

    @Column(nullable = false)
    private Long version;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Station(StationType type)
    {
        this.stationId = UUID.randomUUID().toString();
        this.type = type;
        this.version = 1L;
        this.updatedAt = Instant.now();
    }
}
