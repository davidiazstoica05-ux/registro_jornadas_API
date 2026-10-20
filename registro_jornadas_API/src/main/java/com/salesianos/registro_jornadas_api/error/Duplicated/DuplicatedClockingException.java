package com.salesianos.registro_jornadas_api.error.Duplicated;

public class DuplicatedClockingException extends RuntimeException {
    public DuplicatedClockingException(String message) {
        super(message);
    }
}
