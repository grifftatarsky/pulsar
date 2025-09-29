package com.gpt.pulsarproducer.real.mappers;


import com.gpt.pulsarproducer.real.domain.EquipmentTypeIdentifier;
import com.gpt.pulsarproducer.real.dto.EquipmentTypeIdentifierDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@NoArgsConstructor
public class EquipmentTypeIdentifierMapper
{
    public EquipmentTypeIdentifier toEntity(EquipmentTypeIdentifierDTO dto)
    {
        if (dto == null)
        {
            return null;
        }

        return new EquipmentTypeIdentifier(
            dto.getIdentifierId(),
            dto.getIdentifier(),
            dto.getDescription(),
            dto.getEquipmentTypes()
        );
    }

    public EquipmentTypeIdentifierDTO toDTO(EquipmentTypeIdentifier entity)
    {
        if (entity == null)
        {
            return null;
        }

        return new EquipmentTypeIdentifierDTO(
            entity.getIdentifierId(),
            entity.getIdentifier(),
            entity.getDescription(),
            entity.getEquipmentTypes()
        );
    }
}
