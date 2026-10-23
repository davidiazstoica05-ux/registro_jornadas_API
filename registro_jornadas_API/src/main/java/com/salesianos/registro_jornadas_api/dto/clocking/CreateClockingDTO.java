package com.salesianos.registro_jornadas_api.dto.clocking;

import com.salesianos.registro_jornadas_api.model.Clocking;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.model.Type;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record CreateClockingDTO (




        @NotNull(message = "{clocking.moment.notnull}")
        @PastOrPresent(message = "{clocking.moment.pastorpresent}")
        LocalDateTime moment,

        @NotNull(message = "{clocking.type.notnull}")
        Type type,

        @NotNull(message = "{clocking.employee.notnull}")
        @Min(value = 1, message = "{clocking.employee.min}")
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


