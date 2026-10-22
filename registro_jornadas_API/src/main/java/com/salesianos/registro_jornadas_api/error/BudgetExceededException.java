package com.salesianos.registro_jornadas_api.error;

public class BudgetExceededException extends RuntimeException {

    public BudgetExceededException(String message) {
        super(message);
    }

    public BudgetExceededException(java.math.BigDecimal total, java.math.BigDecimal budget) {
        super("Salaries total " + total + " exceeds the department's budget " + budget);
    }
}
