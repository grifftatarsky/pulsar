package com.gpt.pulsarconsumer.real.mappers;


import com.gpt.pulsarproducer.real.domain.Equipment;
import com.gpt.pulsarproducer.real.domain.EquipmentIdentifier;
import com.gpt.pulsarproducer.real.domain.Station;
import com.gpt.pulsarproducer.real.dto.EquipmentIdentifierDTO;
import com.gpt.pulsarproducer.real.dto.SimpleEquipmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
public class EquipmentIdentifierMapper
{
    private final BaseMapperHelper baseMapperHelper;
    private final EquipmentTypeIdentifierMapper equipmentTypeIdentifierMapper;

    public EquipmentIdentifier toEntity(EquipmentIdentifierDTO dto)
    {
        if (dto == null)
        {
            return null;
        }

        EquipmentIdentifier entity = new EquipmentIdentifier(
            dto.getEquipmentIdentifierId(),
            null,
            equipmentTypeIdentifierMapper.toEntity(dto.getTypeIdentifier()),
            dto.getValue()
        );

        // Handle equipment conversion from SimpleEquipmentDTO
        if (dto.getEquipment() != null)
        {
            Station station = null;

            // Create station from SimpleEquipmentDTO's stationId if present
            if (dto.getEquipment().getStation() != null)
            {
                station = new Station(
                    dto.getEquipment().getStation()
                );
            }

            Equipment equipment =
                baseMapperHelper.toEquipmentEntityFromSimple(dto.getEquipment(), station);

            entity.setEquipment(equipment);
        }

        return entity;
    }

    public EquipmentIdentifierDTO toDTO(EquipmentIdentifier entity)
    {
        if (entity == null)
        {
            return null;
        }

        SimpleEquipmentDTO equipmentDTO = null;

        if (entity.getEquipment() != null)
        {
            equipmentDTO = baseMapperHelper.toSimpleEquipmentDTO(entity.getEquipment());
        }

        return new EquipmentIdentifierDTO(
            entity.getEquipmentIdentifierId(),
            equipmentDTO,
            equipmentTypeIdentifierMapper.toDTO(entity.getTypeIdentifier()),
            entity.getValue()
        );
    }
}
