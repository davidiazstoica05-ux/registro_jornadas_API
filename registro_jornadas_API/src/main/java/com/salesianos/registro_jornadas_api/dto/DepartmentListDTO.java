package com.salesianos.registro_jornadas_api.dto;

import com.salesianos.registro_jornadas_api.model.Department;

import java.math.BigDecimal;

public record DepartmentListDTO(


    Long idDepartment,
    String name,
    BigDecimal budget
){


    public static DepartmentListDTO of(Department department){


        return new DepartmentListDTO(


                department.getIdDepartment(),
                department.getName(),
                department.getBudget()


        );


    }



}
