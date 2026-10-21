package com.salesianos.registro_jornadas_api.dto.department;

import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.validation.UniqueDepartmentName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;

@Schema(
        name = "EditDepartmentDTO",
        description = "Input DTO "
)
public record EditDepartmentDTO(

        @UniqueDepartmentName(message = "{editDepartmentDTO.name.UniqueDepartmentName}")
        @NotBlank(message = "{editDepartmentDTO.name.notBlank}")
        String name,


        @Positive (message = "{editDepartmentDTO.name.positive}")
        BigDecimal budget
){


    public Department to(){

        return Department.builder()
                .name(name)
                .budget(budget)
                .build();


    }

}
