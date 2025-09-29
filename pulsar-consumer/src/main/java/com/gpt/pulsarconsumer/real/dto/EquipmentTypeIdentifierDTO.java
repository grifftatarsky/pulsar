package com.gpt.pulsarconsumer.real.dto;


import com.gpt.pulsarproducer.real.dto.EquipmentType;
import com.gpt.pulsarproducer.real.dto.PersistableUpdateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
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
public class EquipmentTypeIdentifierDTO extends PersistableUpdateDTO
{
    private Long identifierId;

    @NotBlank
    private String identifier;

    private String description;

    @NotEmpty
    private Set<EquipmentType> equipmentTypes = new HashSet<>();
}
