package com.gpt.pulsarproducer.real.domain.base.repository;


import com.gpt.pulsarproducer.real.domain.EquipmentTypeIdentifier;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentTypeIdentifierRepository
    extends JpaRepository<EquipmentTypeIdentifier, Long>,
            PagingAndSortingRepository<EquipmentTypeIdentifier, Long>,
            JpaSpecificationExecutor<EquipmentTypeIdentifier>
{
    Optional<EquipmentTypeIdentifier> findByIdentifier(String identifier);
}
