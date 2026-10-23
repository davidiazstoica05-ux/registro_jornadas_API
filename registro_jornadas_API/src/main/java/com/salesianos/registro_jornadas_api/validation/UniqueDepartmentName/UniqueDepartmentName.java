package com.salesianos.registro_jornadas_api.validation.UniqueDepartmentName;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueDepartmentNameValidator.class)
public @interface UniqueDepartmentName {

    String message() default "The department's name has to be unique";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
