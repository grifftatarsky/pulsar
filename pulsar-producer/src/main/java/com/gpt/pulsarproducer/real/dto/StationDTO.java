package com.gpt.pulsarproducer.real.dto;


import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO extends PersistableUpdateDTO
{
    private Long stationId;

    @NotBlank
    private String callSign;

    private String name;

    private String city;

    private String state;

    private String country;

    private Integer wmoIndex;

    private Integer wmoRegion;

    private LocalDateTime endDate;

    private boolean validated;

    private List<SimpleEquipmentDTO> equipments = new ArrayList<>();

    public StationDTO(SimpleStationDTO dto)
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
        this.equipments = new ArrayList<>();
    }
}
