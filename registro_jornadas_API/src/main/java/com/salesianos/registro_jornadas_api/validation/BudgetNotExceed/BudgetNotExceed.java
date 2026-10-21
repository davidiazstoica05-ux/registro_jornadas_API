package com.salesianos.registro_jornadas_api.validation.BudgetNotExceed;

import com.salesianos.registro_jornadas_api.validation.UniqueDepartmentName.UniqueDepartmentNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BudgetNotExceedValidator.class)
public @interface BudgetNotExceed {


    String message() default "The department's name has to be unique";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};






}
