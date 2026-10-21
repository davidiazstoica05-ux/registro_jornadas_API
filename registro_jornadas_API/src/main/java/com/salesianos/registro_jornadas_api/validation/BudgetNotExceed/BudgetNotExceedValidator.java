package com.salesianos.registro_jornadas_api.validation.BudgetNotExceed;

import com.salesianos.registro_jornadas_api.dto.employee.EditEmployeeDTO;
import com.salesianos.registro_jornadas_api.dto.employee.EmployeeDTO;
import com.salesianos.registro_jornadas_api.error.notfound.DepartmentNotFoundException;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class BudgetNotExceedValidator implements
        ConstraintValidator<BudgetNotExceed, EditEmployeeDTO> {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public boolean isValid(EditEmployeeDTO editEmployeeDTO, ConstraintValidatorContext context) {

        if (editEmployeeDTO.departmentId() == null || editEmployeeDTO.salary() == null) {
            return true;
        }

        Optional<Department> department = departmentRepository.findById(editEmployeeDTO.departmentId());
        if (department.isEmpty()) {
            return true;
        }

        BigDecimal total = employeeRepository.sumSalariesByDepartment(editEmployeeDTO.departmentId())
                .add(editEmployeeDTO.salary());

        return total.compareTo(department.get().getBudget()) <= 0;
    }
}
