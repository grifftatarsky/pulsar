package com.gpt.pulsarconsumer.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    public Station(String stationId, StationType type, Long version, Instant updatedAt)
    {
        this.stationId = stationId;
        this.type = type;
        this.version = version;
        this.updatedAt = updatedAt;
    }
}
