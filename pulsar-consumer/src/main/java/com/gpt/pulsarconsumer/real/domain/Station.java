package com.gpt.pulsarconsumer.real.domain;


import com.gpt.pulsarproducer.real.domain.Equipment;
import com.gpt.pulsarproducer.real.domain.StationDatabaseConstants;
import com.gpt.pulsarproducer.real.domain.base.models.auditing.PersistableUpdateEntity;
import com.gpt.pulsarproducer.real.dto.SimpleStationDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_STATION_CALL_SIGN;
import static com.gpt.pulsarproducer.real.domain.StationDatabaseConstants.COL_STATION_ID;

@Getter
@Setter
@Entity
@Validated
@ToString(exclude = "equipments")
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = StationDatabaseConstants.TBL_STATION,
    uniqueConstraints = @UniqueConstraint(columnNames = { COL_STATION_CALL_SIGN })
)
@NamedQuery(name = "Station.findAll", query = "SELECT s FROM Station s")
public class Station extends PersistableUpdateEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_STATION_ID, updatable = false, nullable = false)
    private Long stationId;

    @NotEmpty
    @Column(name = COL_STATION_CALL_SIGN, nullable = false, length = 16, unique = true)
    private String callSign;

    @Column
    private String name;

    // TODO can we use our postGIS tables somehow to have more of a validated location for city / state???
    @Column
    private String city;

    @Column(length = 5)
    private String state;

    @Column
    private String country;

    // Used for sql queries to know if we should get the location from here or the metadata
    // TODO | Madi | rename to movable or something easier to remember related to function.
    @Column
    private Integer wmoIndex;

    @Column
    private Integer wmoRegion;

    /** the last date that this station is active. if the station is active, this is null */
    @Column
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Equipment> equipments = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private boolean validated = false;

    public Station(SimpleStationDTO dto)
    {
        this.stationId = dto.getStationId();
        this.callSign = dto.getCallSign();
        this.name = dto.getName();
        this.city = dto.getCity();
        this.state = dto.getState();
        this.country = dto.getCountry();
        this.wmoIndex = dto.getWmoIndex();
        this.wmoRegion = dto.getWmoRegion();
        this.endDate = dto.getEndDate();
        this.validated = dto.isValidated();
    }
}