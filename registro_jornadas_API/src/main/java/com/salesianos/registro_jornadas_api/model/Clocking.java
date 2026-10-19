package com.salesianos.registro_jornadas_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clocking {


    @Id
    @GeneratedValue
    Long id;

    LocalDateTime moment; //Mirar anotaciones tipo @PresentOrFuture

    Type type;

    @ManyToOne
    @JoinColumn(name = "idEmpleado" )
    Employee employee;


}
