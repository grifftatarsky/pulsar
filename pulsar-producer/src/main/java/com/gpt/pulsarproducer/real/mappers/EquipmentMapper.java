package com.gpt.pulsarproducer.real.mappers;


import com.gpt.pulsarproducer.real.domain.Equipment;
import com.gpt.pulsarproducer.real.domain.Station;
import com.gpt.pulsarproducer.real.dto.EquipmentDTO;
import com.gpt.pulsarproducer.real.dto.SimpleEquipmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
public class EquipmentMapper
{
    private final BaseMapperHelper baseMapperHelper;

    /**
     * Maps a Equipment entity to a EquipmentDTO.
     *
     * @param equipment
     *     The Equipment entity
     *
     * @return The EquipmentDTO.
     */
    public EquipmentDTO toDTO(Equipment equipment)
    {
        return baseMapperHelper.toEquipmentDTOWithStation(equipment);
    }

    /**
     * Maps a Equipment entity to a EquipmentDTO.
     *
     * @param equipment
     *     The Equipment entity
     *
     * @return The EquipmentDTO.
     */
    public SimpleEquipmentDTO toSimpleDTO(Equipment equipment)
    {
        return baseMapperHelper.toSimpleEquipmentDTO(equipment);
    }

    /**
     * Maps a EquipmentDTO to a Equipment entity.
     *
     * @param dto
     *     The EquipmentDTO.
     *
     * @return The Equipment entity.
     */
    public Equipment toEntity(EquipmentDTO dto)
    {
        if (dto == null)
        {
            return null;
        }

        // Convert SimpleStationDTO to Station entity if present
        Station station = null;
        if (dto.getStation() != null)
        {
            station = new Station(
                dto.getStation().getStationId(),
                dto.getStation().getCallSign(),
                dto.getStation().getName(),
                dto.getStation().getCity(),
                dto.getStation().getState(),
                dto.getStation().getCountry(),
                dto.getStation().getWmoIndex(),
                dto.getStation().getWmoRegion(),
                dto.getStation().getEndDate(),
                new java.util.ArrayList<>(),
                dto.getStation().isValidated()
            );
        }

        return baseMapperHelper.toEquipmentEntity(dto, station);
    }
}
