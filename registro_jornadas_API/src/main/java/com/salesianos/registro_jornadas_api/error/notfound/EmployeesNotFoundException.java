package com.salesianos.registro_jornadas_api.error.notfound;

public class EmployeesNotFoundException extends RuntimeException {
    public EmployeesNotFoundException(String message) {
        super(message);
    }


    public EmployeesNotFoundException() {
        super("departments not found ");

    }

    public EmployeesNotFoundException(Long id){


        super("Department not found with id " + id);

    }



}
