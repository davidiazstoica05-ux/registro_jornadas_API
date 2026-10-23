package com.salesianos.registro_jornadas_api.dto.department;
import com.salesianos.registro_jornadas_api.model.Department;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


@Schema(

        name = "DepartmentDTO",
        description = "Department's name and badge"


)
public record DepartmentDTO(

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

    public static DepartmentDTO of(Department d) {


        return new DepartmentDTO(
                d.getName(),
                d.getBudget()
        );




    }




}
