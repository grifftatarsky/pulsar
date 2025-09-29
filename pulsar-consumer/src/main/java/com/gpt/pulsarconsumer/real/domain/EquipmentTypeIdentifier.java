package com.gpt.pulsarconsumer.real.domain;


import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableUpdateEntity;
import com.gpt.pulsarproducer.real.domain.validation.annotation.ValidEquipmentTypeIdentifier;
import com.gpt.pulsarproducer.real.dto.EquipmentType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_EQUIPMENT_TYPE;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_IDENTIFIER;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_IDENTIFIER_ID;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.TBL_EQUIPMENT_TYPE_IDENTIFIERS;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.TBL_EQUIPMENT_TYPE_IDENTIFIERS_EQUIPMENT_TYPE;

@Entity
@Getter
@Setter
@ToString
@Validated
@ValidEquipmentTypeIdentifier
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = TBL_EQUIPMENT_TYPE_IDENTIFIERS,
    uniqueConstraints = @UniqueConstraint(columnNames = { COL_IDENTIFIER })
)
@NamedQuery(name = "EquipmentTypeIdentifier.findAll", query = "SELECT e FROM EquipmentTypeIdentifier e")
public class EquipmentTypeIdentifier extends PersistableUpdateEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_IDENTIFIER_ID, updatable = false, nullable = false)
    private Long identifierId;

    @NotBlank
    @Column(name = COL_IDENTIFIER, nullable = false)
    private String identifier;

    @Column
    private String description;

    // set prevents duplicates
    @NotEmpty
    @Enumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = TBL_EQUIPMENT_TYPE_IDENTIFIERS_EQUIPMENT_TYPE,
        joinColumns = @JoinColumn(name = COL_IDENTIFIER_ID)
    )
    @Column(name = COL_EQUIPMENT_TYPE)
    private Set<EquipmentType> equipmentTypes = new HashSet<>();
}
