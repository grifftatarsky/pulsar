package com.gpt.pulsarproducer.real.domain.base.repository;


import com.gpt.pulsarproducer.real.domain.Equipment;
import com.gpt.pulsarproducer.real.dto.EquipmentType;
import jakarta.validation.constraints.NotEmpty;
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
public interface EquipmentRepository
    extends JpaRepository<Equipment, Long>,
            PagingAndSortingRepository<Equipment, Long>,
            JpaSpecificationExecutor<Equipment>
{
    Optional<Equipment> findByStationStationIdAndEquipmentType(
        @NotNull Long stationId, @NotNull EquipmentType equipmentType);

    Optional<Equipment> findByStationCallSignAndEquipmentType(
        @NotEmpty String callSign, @NotNull EquipmentType equipmentType);

    List<Equipment> findAllByStationStationId(@NotNull Long stationId);
}
