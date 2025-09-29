package com.gpt.pulsarproducer.real.domain;


import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableUpdateEntity;
import com.gpt.pulsarproducer.real.domain.validation.annotation.ValidEquipment;
import com.gpt.pulsarproducer.real.dto.EquipmentType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.geolatte.geom.Point;
import org.geolatte.geom.Position;
import org.springframework.validation.annotation.Validated;

import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_EQUIPMENT_ID;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_EQUIPMENT_TYPE;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_STATION_ID;

@Getter
@Setter
@Entity
@ToString
@Validated
@ValidEquipment
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = StationDatabaseConstants.TBL_EQUIPMENT,
    uniqueConstraints = @UniqueConstraint(columnNames = { COL_STATION_ID, COL_EQUIPMENT_TYPE })
)
@NamedQuery(name = "Equipment.findAll", query = "SELECT e FROM Equipment e")
public class Equipment extends PersistableUpdateEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_EQUIPMENT_ID, updatable = false, nullable = false)
    private Long equipmentId;

    @NotNull
    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER,
        cascade = { CascadeType.ALL }
    )
    @JoinColumn(
        name = COL_STATION_ID,
        referencedColumnName = COL_STATION_ID,
        nullable = false
    )
    private Station station;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;

    @Column
    private Point<? extends Position> location;

    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    @NotNull
    @Column(nullable = false)
    private boolean validated = false;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EquipmentIdentifier> identifiers = new ArrayList<>();
}
