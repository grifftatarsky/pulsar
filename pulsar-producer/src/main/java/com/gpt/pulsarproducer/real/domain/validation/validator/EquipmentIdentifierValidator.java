package com.gpt.pulsarproducer.real.domain.validation.validator;


import com.gpt.pulsarproducer.real.domain.EquipmentIdentifier;
import com.gpt.pulsarproducer.real.domain.validation.ValidatorHelperUtil;
import com.gpt.pulsarproducer.real.domain.validation.annotation.ValidEquipmentIdentifier;
import com.gpt.pulsarproducer.real.dto.EquipmentValidationStrategy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class EquipmentIdentifierValidator implements ConstraintValidator<ValidEquipmentIdentifier, EquipmentIdentifier>,
                                                     EquipmentValidationStrategy
{

    @Override
    public boolean isValid(EquipmentIdentifier identifier, ConstraintValidatorContext context)
    {
        List<String> messages = new ArrayList<>();

        boolean failValidation = false;

        if (identifier.getTypeIdentifier() == null)
        {
            messages.add("EquipmentTypeIdentifier is required.");
            failValidation = true;
        }

        if (identifier.getEquipment() == null)
        {
            messages.add("Equipment is required.");
            failValidation = true;
        }

        if (!failValidation)
        {
            if (CollectionUtils.isEmpty(identifier.getTypeIdentifier().getEquipmentTypes()))
            {
                messages.add("EquipmentTypeIdentifier.EquipmentTypes cannot be null or empty.");
            }
            else
            {
                // if the equipmentType of the equipment is not in the list of identifiers, we fail here
                if (!identifier.getTypeIdentifier().getEquipmentTypes()
                               .contains(identifier.getEquipment().getEquipmentType()))
                {
                    messages.add(
                        "Equipment.EquipmentType is not in the list of allowed EquipmentTypes. "
                            + "Cannot associate this Equipment to this EquipmentIdentifier.");
                }
            }
        }

        return ValidatorHelperUtil.checkIfValidationMessagesExist(messages, context);
    }
}