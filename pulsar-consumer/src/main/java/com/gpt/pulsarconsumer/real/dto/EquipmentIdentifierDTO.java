package com.gpt.pulsarconsumer.real.dto;


import com.gpt.pulsarproducer.real.dto.EquipmentTypeIdentifierDTO;
import com.gpt.pulsarproducer.real.dto.PersistableUpdateDTO;
import com.gpt.pulsarproducer.real.dto.SimpleEquipmentDTO;
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
public class EquipmentIdentifierDTO extends PersistableUpdateDTO
{
    private Long equipmentIdentifierId;

    @NotNull
    private SimpleEquipmentDTO equipment;

    @NotNull
    private EquipmentTypeIdentifierDTO typeIdentifier;

    @NotBlank
    private String value;
}
