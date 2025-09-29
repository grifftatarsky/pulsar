package com.gpt.pulsarproducer.real.domain.validation.annotation;


import com.gpt.pulsarproducer.real.domain.validation.validator.EquipmentTypeIdentifierValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EquipmentTypeIdentifierValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEquipmentTypeIdentifier
{
    String message() default "EquipmentTypeIdentifier does not meet requirements.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}