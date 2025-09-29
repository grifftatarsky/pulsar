package com.gpt.pulsarproducer.real.dto;


import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geolatte.geom.Point;
import org.geolatte.geom.Position;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ValidEquipmentDto
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEquipmentDTO extends PersistableUpdateDTO
{
    private Long equipmentId;

    @NotNull
    private SimpleStationDTO station;

    @NotNull
    private EquipmentType equipmentType;

    private Point<? extends Position> location;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private boolean validated;

    private List<SimpleEquipmentIdentifierDTO> identifiers = new ArrayList<>();

    public SimpleEquipmentDTO(EquipmentDTO dto)
    {
        this.equipmentId = dto.getEquipmentId();
        this.station = dto.getStation();
        this.equipmentType = dto.getEquipmentType();
        this.location = dto.getLocation();
        this.startDateTime = dto.getStartDateTime();
        this.endDateTime = dto.getEndDateTime();
        this.validated = dto.isValidated();
        this.identifiers = dto.getIdentifiers();
    }
}