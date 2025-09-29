package com.gpt.pulsarconsumer.real.domain.base.repository;


import com.gpt.pulsarproducer.real.domain.EquipmentIdentifier;
import com.gpt.pulsarproducer.real.dto.EquipmentType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentIdentifierRepository
    extends JpaRepository<EquipmentIdentifier, Long>,
            PagingAndSortingRepository<EquipmentIdentifier, Long>,
            JpaSpecificationExecutor<EquipmentIdentifier>
{
    Integer countByTypeIdentifierIdentifierIdAndEquipmentEquipmentType(
        @NotNull Long identifierId,
        @NotNull EquipmentType equipmentType
    );
}