package com.salesianos.registro_jornadas_api.service;

import com.salesianos.registro_jornadas_api.dto.employee.EditEmployeeDTO;
import com.salesianos.registro_jornadas_api.error.notfound.DepartmentNotFoundException;
import com.salesianos.registro_jornadas_api.error.notfound.EmployeesNotFoundException;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public  Employee getById(Long id) {


    return employeeRepository.findById(id).orElseThrow(() -> new EmployeesNotFoundException(id));


    }


    public Employee addEmployee(Department department, EditEmployeeDTO editEmployeeDTO){


       Department department1 = departmentRepository.findById(department
                                                    .getIdDepartment())
                                                    .orElseThrow(() -> new DepartmentNotFoundException(editEmployeeDTO.departmentId()));


      return employeeRepository.save(editEmployeeDTO.to(department1));

    }


    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();



    }

    public Employee updateEmployee (Long id,  EditEmployeeDTO employee) {


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
                .orElseThrow(() -> new EmployeesNotFoundException(id));



    }

    public void deleteEmployee(@Min(value = 1) Long id) {


        employeeRepository.deleteById(id);




    }
}
