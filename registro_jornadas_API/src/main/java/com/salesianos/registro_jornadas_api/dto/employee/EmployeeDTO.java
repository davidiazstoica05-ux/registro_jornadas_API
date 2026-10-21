package com.salesianos.registro_jornadas_api.dto.employee;


import com.salesianos.registro_jornadas_api.dto.department.DepartmentDTO;
import com.salesianos.registro_jornadas_api.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(
        name = "EmployeeDTO",
        description = "Employee's data returned by the API"
)
public record EmployeeDTO(


        @Schema(
                description = "Employee's full name",
                example = "Ana García López"
        )
        String fullName,

        @Schema(
                description = "Employee's position",
                example = "Desarrolladora senior"
        )
        String position,

        @Schema(
                description = "Employee's salary",
                example = "3500.00"
        )
        BigDecimal salary,


        @Schema(
                description = "Name of the employee's department, or 'Sin departamento' if it has none",
                example = "Desarrollo"
        )
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
