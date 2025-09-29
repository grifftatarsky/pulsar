package com.gpt.pulsarconsumer.real.dto;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EquipmentDTOValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEquipmentDto
{
    String message() default "Equipment does not meet requirements.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}