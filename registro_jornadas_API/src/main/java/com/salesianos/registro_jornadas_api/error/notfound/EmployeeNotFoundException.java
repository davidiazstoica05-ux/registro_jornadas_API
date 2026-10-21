package com.salesianos.registro_jornadas_api.error.notfound;

import jakarta.persistence.EntityNotFoundException;

public class EmployeeNotFoundException extends EntityNotFoundException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }


    public EmployeeNotFoundException() {
        super("Employee not found ");

    }

    public EmployeeNotFoundException(Long id){


        super("Employee not found with id " + id);

    }



}
