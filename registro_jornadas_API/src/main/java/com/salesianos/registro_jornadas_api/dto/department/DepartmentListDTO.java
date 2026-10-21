package com.salesianos.registro_jornadas_api.dto.department;

import com.salesianos.registro_jornadas_api.model.Department;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


@Schema(
        name = "DepartmentListDTO",
        description = "A list of diferents departments "
)
public record DepartmentListDTO(

        @Schema(
                description = "Department's name ",
                example = "Develop"
        )
        String name,

        @Schema(
                description = "Department's budget",
                example = "2000"
        )
        BigDecimal budget
){


    public static DepartmentListDTO of(Department department){


        return new DepartmentListDTO(


                department.getName(),
                department.getBudget()


        );


    }



}
