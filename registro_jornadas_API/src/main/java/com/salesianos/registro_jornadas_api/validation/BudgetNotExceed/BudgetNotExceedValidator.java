package com.salesianos.registro_jornadas_api.validation.BudgetNotExceed;

import com.salesianos.registro_jornadas_api.dto.employee.CreateEmployeeDTO;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class BudgetNotExceedValidator implements
        ConstraintValidator<BudgetNotExceed, CreateEmployeeDTO> {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public boolean isValid(CreateEmployeeDTO createEmployeeDTO, ConstraintValidatorContext context) {

        if (createEmployeeDTO.departmentId() == null || createEmployeeDTO.salary() == null) {
            return true;
        }

        Optional<Department> department = departmentRepository.findById(createEmployeeDTO.departmentId());

        // Al editar: si ni el salario ni el departamento han cambiado, no hay nada que comprobar
        if (createEmployeeDTO.employeeID() != null) {
            Optional<Employee> old = employeeRepository.findById(createEmployeeDTO.employeeID());

            if (old.isPresent()
                    && old.get().getDepartment() != null
                    && old.get().getDepartment().getIdDepartment().equals(createEmployeeDTO.departmentId())
                    && old.get().getSalary().compareTo(createEmployeeDTO.salary()) == 0) {
                return true;
            }
        }

        // En el alta (id null) suma todo. Al editar, excluye al propio empleado
        BigDecimal currentSalaries = createEmployeeDTO.employeeID() == null
                ? employeeRepository.sumSalariesByDepartment(createEmployeeDTO.departmentId())
                : employeeRepository.sumSalariesByDepartmentExcludingEmployee(
                createEmployeeDTO.departmentId(), createEmployeeDTO.employeeID());

        BigDecimal total = currentSalaries.add(createEmployeeDTO.salary());

        return department.map(value -> total
                .compareTo(value.getBudget()) <= 0).orElse(true);
    }
}
