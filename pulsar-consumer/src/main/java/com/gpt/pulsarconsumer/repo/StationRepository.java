package com.gpt.pulsarconsumer.repo;


import com.gpt.pulsarconsumer.domain.Station;
import com.gpt.pulsarconsumer.domain.StationType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long>
{
    Optional<Station> findByStationId(String stationId);

    long countByType(StationType type);
}
