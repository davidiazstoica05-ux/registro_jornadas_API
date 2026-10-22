package com.salesianos.registro_jornadas_api.dto.clocking;

import com.salesianos.registro_jornadas_api.model.Clocking;
import com.salesianos.registro_jornadas_api.model.Type;

import java.time.LocalDateTime;

public record ClockDTO(

        LocalDateTime moment,

        Type type,

        String nombreEmpleado

) {


    public static ClockDTO of(Clocking clocking){


        return new ClockDTO(

                clocking.getMoment(),
                clocking.getType(),
                clocking.getEmployee().getFullName()


        );



    }



}
