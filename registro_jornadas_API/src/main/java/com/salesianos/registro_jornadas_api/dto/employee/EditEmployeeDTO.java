package com.salesianos.registro_jornadas_api.dto.employee;

import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Optional;

public record EditEmployeeDTO(


        @NotBlank(message = "{employee.fullname.notblank}")
        @Pattern(regexp = "^\\p{L}+([ '\\-]\\p{L}+)+$", message = "{employee.fullname.pattern}")
        String fullName,

        @NotBlank (message = "{employee.position.notblank}")
        String position,

        @Positive (message = "{employee.salary.positive}")
        @NotNull
        BigDecimal salary,


        Long  departmentId



) {

    public Employee to (Department department){


        return Employee.builder()
                .fullName(fullName)
                .position(position)
                .salary(salary)
                .department(department )
                .build();

    }




}
