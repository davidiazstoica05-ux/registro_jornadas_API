package com.salesianos.registro_jornadas_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue
    Long idEmpleado;

    String  fullName;
    String  position;

    BigDecimal salary;


    @ManyToOne
    @JoinColumn(name = "idDepartment")
    Department department;





}
