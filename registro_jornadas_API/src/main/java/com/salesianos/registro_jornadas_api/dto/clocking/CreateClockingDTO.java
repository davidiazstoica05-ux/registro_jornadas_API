package com.salesianos.registro_jornadas_api.dto.clocking;

import com.salesianos.registro_jornadas_api.model.Clocking;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.model.Type;

import java.time.LocalDateTime;

public record CreateClockingDTO (



        LocalDateTime moment,

        Type type,

        Long idEmpleado
) {






    public  Clocking to(Employee employee){


        return Clocking.builder()
                .moment(moment)
                .type(type)
                .employee(employee)
                .build();

    }



}


