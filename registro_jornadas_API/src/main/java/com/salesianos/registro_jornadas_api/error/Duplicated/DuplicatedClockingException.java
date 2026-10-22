package com.salesianos.registro_jornadas_api.error.Duplicated;

import com.salesianos.registro_jornadas_api.model.Type;

public class DuplicatedClockingException extends RuntimeException {
    public DuplicatedClockingException(String message) {
        super(message);
    }

    public DuplicatedClockingException(Long employeeId, Type type) {
        super("Employee %d already has a %s as last clocking".formatted(employeeId, type));
    }
}
