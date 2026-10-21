package com.salesianos.registro_jornadas_api.validation.UniqueDepartmentName;

import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;


//De esta forma se crean la validaciones propias
@RequiredArgsConstructor
public class UniqueDepartmentNameValidator implements
                ConstraintValidator<UniqueDepartmentName, String> {


    private final DepartmentRepository departmentRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.hasText(value) && !departmentRepository.existsByName(value);
    }



}
