package com.salesianos.registro_jornadas_api.service;

import com.salesianos.registro_jornadas_api.dto.employee.CreateEmployeeDTO;
import com.salesianos.registro_jornadas_api.error.BudgetExceededException;
import com.salesianos.registro_jornadas_api.error.notfound.DepartmentNotFoundException;
import com.salesianos.registro_jornadas_api.error.notfound.EmployeeNotFoundException;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public  Employee getById(Long id) {


    return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));


    }


    public Employee addEmployee( CreateEmployeeDTO editEmployeeDTO){


        Department department = null;

        if (editEmployeeDTO.departmentId() != null){

            department = departmentRepository.findById(editEmployeeDTO.departmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(
                            editEmployeeDTO.departmentId()
                    ));

        }



      return employeeRepository.save(editEmployeeDTO.to(department));

    }


    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();



    }

    public Employee updateEmployee (Long id,  CreateEmployeeDTO employee) {


        return employeeRepository.findById(id)
                                  .map(e -> {
                                      e.setSalary(employee.salary());
                                      e.setFullName(employee.fullName());
                                      e.setPosition(employee.position());
                                      e.setDepartment(departmentRepository.findById(employee
                                                      .departmentId())
                                                      .orElseThrow(() -> new DepartmentNotFoundException(id)));
                                      return employeeRepository.save(e);
                                  })
                .orElseThrow(() -> new EmployeeNotFoundException(id));



    }

    public void deleteEmployee(@Min(value = 1) Long id) {


        employeeRepository.deleteById(id);



    }



    public Employee switchDepartment (Long employeeId, Long departmentId){

        return employeeRepository.findById(employeeId)
                .map(e -> {

                    if (departmentId == null) {
                        e.setDepartment(null);
                    } else {
                        Department department = departmentRepository.findById(departmentId)
                                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

                        BigDecimal total = employeeRepository
                                .sumSalariesByDepartmentExcludingEmployee(departmentId, employeeId)
                                .add(e.getSalary());

                        if (total.compareTo(department.getBudget()) > 0) {
                            throw new BudgetExceededException(total, department.getBudget());
                        }

                        e.setDepartment(department);
                    }

                    return employeeRepository.save(e);

                })
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }



}






