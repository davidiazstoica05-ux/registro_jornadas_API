package com.salesianos.registro_jornadas_api.dto.employee;

import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record EditEmployeeDTO(

        @NotBlank
        String fullName,

        @NotBlank
        String position,

        BigDecimal salary,


        Long departmentId



) {

    public Employee to (Department department){


        return Employee.builder()
                .fullName(fullName)
                .position(position)
                .salary(salary)
                .department(department)
                .build();

    }




}
