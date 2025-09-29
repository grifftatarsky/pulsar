package com.gpt.pulsarproducer.real.domain;


import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableUpdateEntity;
import com.gpt.pulsarproducer.real.domain.validation.annotation.ValidEquipmentIdentifier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_EQUIPMENT_ID;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_EQUIPMENT_IDENTIFIER_ID;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_IDENTIFIER_ID;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.TBL_EQUIPMENT_IDENTIFIERS;

@Entity
@Getter
@Setter
@ToString(exclude = "equipment")
@Validated
@NoArgsConstructor
@AllArgsConstructor
@ValidEquipmentIdentifier
@Table(
    name = TBL_EQUIPMENT_IDENTIFIERS,
    uniqueConstraints = @UniqueConstraint(columnNames = { COL_EQUIPMENT_ID, COL_IDENTIFIER_ID })
)
@NamedQuery(name = "EquipmentIdentifier.findAll", query = "SELECT ei FROM EquipmentIdentifier ei")
public class EquipmentIdentifier extends PersistableUpdateEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_EQUIPMENT_IDENTIFIER_ID, updatable = false, nullable = false)
    private Long equipmentIdentifierId;

    @NotNull
    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = COL_EQUIPMENT_ID,
        referencedColumnName = COL_EQUIPMENT_ID,
        nullable = false
    )
    private Equipment equipment;

    @NotNull
    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = COL_IDENTIFIER_ID,
        referencedColumnName = COL_IDENTIFIER_ID,
        nullable = false
    )
    private EquipmentTypeIdentifier typeIdentifier;

    @NotBlank
    @Column(nullable = false)
    private String value;
}
