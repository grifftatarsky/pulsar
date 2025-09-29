package com.gpt.pulsarproducer.real.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@ToString
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEquipmentIdentifierDTO extends PersistableUpdateDTO
{
    private Long equipmentIdentifierId;

    @NotNull
    private Long equipmentId;

    @NotNull
    private EquipmentTypeIdentifierDTO typeIdentifier;

    @NotBlank
    private String value;

    public SimpleEquipmentIdentifierDTO(EquipmentIdentifierDTO dto)
    {
        this.equipmentIdentifierId = dto.getEquipmentIdentifierId();
        this.equipmentId = dto.getEquipment().getEquipmentId();
        this.typeIdentifier = dto.getTypeIdentifier();
        this.value = dto.getValue();
    }
}
