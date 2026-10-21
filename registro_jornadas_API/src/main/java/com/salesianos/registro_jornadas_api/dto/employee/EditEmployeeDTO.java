package com.salesianos.registro_jornadas_api.dto.employee;

import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Optional;

@Schema(
        name = "EditEmployeeDTO",
        description = "Input DTO to create or update an employee"
)
public record EditEmployeeDTO(


        @Schema(
                description = "Employee's full name (at least name and surname)",
                example = "Ana García López",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "{employee.fullname.notblank}")
        @Pattern(regexp = "^\\p{L}+([ '\\-]\\p{L}+)+$", message = "{employee.fullname.pattern}")
        String fullName,

        @Schema(
                description = "Employee's position",
                example = "Desarrolladora senior",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank (message = "{employee.position.notblank}")
        String position,

        @Schema(
                description = "Employee's salary, must be positive",
                example = "3500.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Positive (message = "{employee.salary.positive}")
        @NotNull
        BigDecimal salary,


        @Schema(
                description = "Id of the employee's department. It is optional",
                example = "1",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
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
