package com.gpt.pulsarproducer.real.domain.validation.validator;


import com.gpt.pulsarproducer.real.domain.EquipmentTypeIdentifier;
import com.gpt.pulsarproducer.real.domain.validation.ValidatorHelperUtil;
import com.gpt.pulsarproducer.real.domain.validation.annotation.ValidEquipmentTypeIdentifier;
import com.gpt.pulsarproducer.real.dto.EquipmentValidationStrategy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EquipmentTypeIdentifierValidator
    implements ConstraintValidator<ValidEquipmentTypeIdentifier, EquipmentTypeIdentifier>, EquipmentValidationStrategy
{

    @Override
    public boolean isValid(EquipmentTypeIdentifier identifier, ConstraintValidatorContext context)
    {
        List<String> messages = new ArrayList<>();

        validateNoDuplicateEquipmentTypes(messages, identifier.getEquipmentTypes());

        return ValidatorHelperUtil.checkIfValidationMessagesExist(messages, context);
    }
}