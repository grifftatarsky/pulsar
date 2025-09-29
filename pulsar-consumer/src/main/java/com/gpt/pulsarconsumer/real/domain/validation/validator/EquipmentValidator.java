package com.gpt.pulsarconsumer.real.domain.validation.validator;


import com.gpt.pulsarproducer.real.domain.Equipment;
import com.gpt.pulsarproducer.real.domain.validation.ValidatorHelperUtil;
import com.gpt.pulsarproducer.real.domain.validation.annotation.ValidEquipment;
import com.gpt.pulsarproducer.real.dto.EquipmentValidationStrategy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EquipmentValidator implements ConstraintValidator<ValidEquipment, Equipment>, EquipmentValidationStrategy
{

    @Override
    public boolean isValid(Equipment equipment, ConstraintValidatorContext context)
    {
        List<String> messages = new ArrayList<>();

        validateStation(messages, equipment);
        validateEquipmentTypes(messages, equipment);

        return ValidatorHelperUtil.checkIfValidationMessagesExist(messages, context);
    }

    private void validateEquipmentTypes(@NotNull List<String> messages, @NotNull Equipment equipment)
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

    private void validateStation(@NotNull List<String> messages, @NotNull Equipment equipment)
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