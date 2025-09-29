package com.gpt.pulsarproducer.real.mappers;


import com.gpt.pulsarproducer.real.domain.Station;
import com.gpt.pulsarproducer.real.dto.StationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
public class StationMapper
{
    private final BaseMapperHelper baseMapperHelper;

    /**
     * Maps a Station entity to a StationDTO.
     *
     * @param entity
     *     The Station entity.
     *
     * @return The StationDTO.
     */
    public StationDTO toDTO(Station entity)
    {
        return baseMapperHelper.toStationDTOWithEquipments(entity);
    }

    /**
     * Maps a StationDTO to a Station entity.
     *
     * @param dto
     *     The StationDTO.
     *
     * @return The Station entity.
     */
    public Station toEntity(StationDTO dto)
    {
        return baseMapperHelper.toStationEntity(dto);
    }
}
