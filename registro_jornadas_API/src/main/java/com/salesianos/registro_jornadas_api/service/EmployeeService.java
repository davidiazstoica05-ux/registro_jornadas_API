package com.salesianos.registro_jornadas_api.service;

import com.salesianos.registro_jornadas_api.dto.employee.EditEmployeeDTO;
import com.salesianos.registro_jornadas_api.error.notfound.DepartmentNotFoundException;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;


    public Employee addEmployee(Department department, EditEmployeeDTO editEmployeeDTO){


       Department department1 = departmentRepository.findById(department
                                                    .getIdDepartment())
                                                    .orElseThrow(() -> new DepartmentNotFoundException(editEmployeeDTO.departmentId()));


      return employeeRepository.save(editEmployeeDTO.to(department1
       ));

    }


    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();



    }
}
