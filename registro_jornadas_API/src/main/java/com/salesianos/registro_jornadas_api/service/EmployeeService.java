package com.salesianos.registro_jornadas_api.service;

import com.salesianos.registro_jornadas_api.dto.employee.EditEmployeeDTO;
import com.salesianos.registro_jornadas_api.dto.employee.EmployeeDTO;
import com.salesianos.registro_jornadas_api.error.notfound.DepartmentNotFoundException;
import com.salesianos.registro_jornadas_api.error.notfound.EmployeeNotFoundException;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import com.salesianos.registro_jornadas_api.validation.BudgetNotExceed.BudgetNotExceed;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public  Employee getById(Long id) {


    return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));


    }


    public Employee addEmployee( EditEmployeeDTO editEmployeeDTO){


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
                                            e.setDepartment(departmentRepository.findById(departmentId)
                                                    .orElseThrow(() -> new DepartmentNotFoundException(departmentId)
                                                    )
                                            );
                                        }

                                        return employeeRepository.save(e);

                                    })
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));


    }





}
