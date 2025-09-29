package com.gpt.pulsarproducer.real.domain.validation;


import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ValidatorHelperUtil
{
    public static boolean checkIfValidationMessagesExist(
        List<String> messages, @NotNull ConstraintValidatorContext context)
    {
        if (!CollectionUtils.isEmpty(messages))
        {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join(",", messages)).addConstraintViolation();
            return false;
        }
        return true;
    }
}
