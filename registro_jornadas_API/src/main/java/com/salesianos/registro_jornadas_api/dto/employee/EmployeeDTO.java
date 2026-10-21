package com.salesianos.registro_jornadas_api.dto.employee;


import com.salesianos.registro_jornadas_api.dto.department.DepartmentDTO;
import com.salesianos.registro_jornadas_api.model.Employee;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record EmployeeDTO(



        String fullName,


        String position,

        BigDecimal salary,


        String department




) {


        public static EmployeeDTO of(Employee e){


                return new EmployeeDTO(

                        e.getFullName(),
                        e.getPosition(),
                        e.getSalary(),
                        e.getDepartment() != null ? e.getDepartment().getName() : "Sin departamento"

                );


        }



}
