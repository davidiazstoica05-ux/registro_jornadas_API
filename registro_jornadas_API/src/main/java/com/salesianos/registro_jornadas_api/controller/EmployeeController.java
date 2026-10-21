package com.salesianos.registro_jornadas_api.controller;

import com.salesianos.registro_jornadas_api.dto.department.DepartmentDTO;
import com.salesianos.registro_jornadas_api.dto.department.DepartmentListDTO;
import com.salesianos.registro_jornadas_api.dto.department.EditDepartmentDTO;
import com.salesianos.registro_jornadas_api.dto.employee.EditEmployeeDTO;
import com.salesianos.registro_jornadas_api.dto.employee.EmployeeDTO;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {


    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> addEmployee(
            Department department,
            @Valid @RequestBody EditEmployeeDTO editEmployeeDTO
            ){



     return ResponseEntity.ok(EmployeeDTO.of(employeeService.addEmployee( editEmployeeDTO)));


    }


    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAll(Employee e) {

        List<Employee> result = employeeService.getAllEmployees();

        return ResponseEntity.ok(
                result
                        .stream()
                        .map(EmployeeDTO :: of)
                        .toList());

    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById( @PathVariable @Min(value = 1) Long id ){


        return ResponseEntity.ok(

                EmployeeDTO.of(employeeService.getById(id))

        );



    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateDepartment(

            @PathVariable @Min(value = 1) Long id,
            @Valid @RequestBody EditEmployeeDTO employeeDTO
    ){

        return ResponseEntity.ok(

                EmployeeDTO.of(employeeService.updateEmployee(id, employeeDTO))

        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDTO> deleteEmployee(

            @PathVariable @Min(value = 1) Long id

    ){

        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();


    }






}
