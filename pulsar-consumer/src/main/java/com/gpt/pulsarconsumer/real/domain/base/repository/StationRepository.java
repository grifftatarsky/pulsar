package com.gpt.pulsarconsumer.real.domain.base.repository;


import com.gpt.pulsarproducer.real.domain.Station;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

@Validated
@Repository
public interface StationRepository extends JpaRepository<Station, Long>,
                                           PagingAndSortingRepository<Station, Long>,
                                           JpaSpecificationExecutor<Station>
{
    Optional<Station> findByCallSign(@NotBlank String callSign);

    List<Station> findByCallSignIn(@NotNull List<String> callSigns);

    void deleteByStationId(@NotNull Long stationId);
}
