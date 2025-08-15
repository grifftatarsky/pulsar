package com.gpt.pulsarproducer.repo;


import com.gpt.pulsarproducer.domain.Station;
import com.gpt.pulsarproducer.domain.StationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StationRepository extends JpaRepository<Station, Long>
{
    @Query(value = "SELECT * FROM station ORDER BY random() LIMIT 1", nativeQuery = true)
    Station pickRandom();

    long countByType(StationType type);
}
