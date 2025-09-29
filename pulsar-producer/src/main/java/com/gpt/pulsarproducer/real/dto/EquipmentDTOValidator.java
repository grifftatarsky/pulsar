package com.gpt.pulsarproducer.real.dto;


import com.gpt.pulsarproducer.real.domain.validation.ValidatorHelperUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
public class EquipmentDTOValidator
    implements ConstraintValidator<ValidEquipmentDto, EquipmentDTO>, EquipmentValidationStrategy
{

    @Override
    public boolean isValid(EquipmentDTO equipment, ConstraintValidatorContext context)
    {
        List<String> messages = new ArrayList<>();

        validateStation(messages, equipment);
        validateEquipmentTypes(messages, equipment);

        return ValidatorHelperUtil.checkIfValidationMessagesExist(messages, context);
    }

    private void validateEquipmentTypes(@NotNull List<String> messages, @NotNull EquipmentDTO equipment)
    {
        if (equipment.getEquipmentType() == null)
        {
            messages.add("EquipmentType is required.");
        }
        else
        {
            validateEquipmentTypeRules(
                messages,
                equipment.getEquipmentType(),
                equipment.getLocation()
            );
        }
    }

    private void validateStation(@NotNull List<String> messages, @NotNull EquipmentDTO equipment)
    {
        if (equipment.getStation() == null)
        {
            messages.add("Station is required.");
        }
        else
        {
            validateStationIdentifiers(
                messages,
                equipment.getStation().getStationId(),
                equipment.getStation().getCallSign()
            );
        }
    }
}