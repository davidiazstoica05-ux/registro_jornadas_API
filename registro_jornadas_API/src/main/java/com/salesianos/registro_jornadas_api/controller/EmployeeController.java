package com.salesianos.registro_jornadas_api.controller;

import com.salesianos.registro_jornadas_api.dto.employee.CreateEmployeeDTO;
import com.salesianos.registro_jornadas_api.dto.employee.EmployeeDTO;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
@Tag(name = "Employee Controller", description = "An employee's controller")
public class EmployeeController {


    private final EmployeeService employeeService;

    @Operation(summary = "Create an employee", description = "Creates an employee. The department is optional: if departmentId is sent, it must exist")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class),
                            examples = @ExampleObject(value = """
                { "fullName": "Ana García López", "position": "Desarrolladora senior", "salary": 3500.00, "department": "Desarrollo" }
                """))),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Validation Error",
                  "instance": "/api/v1/employee",
                  "invalid-params": [
                    { "object": "editEmployeeDTO", "message": "The salary must be positive", "field": "salary", "rejectedValue": -100 }
                  ]
                }
                """))),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                {
                  "type": "https://example.com/errors/not-found",
                  "title": "Recurso no encontrado",
                  "status": 404,
                  "detail": "Department not found with id 99",
                  "instance": "/api/v1/employee"
                }
                """)))
    })
    @PostMapping
    public ResponseEntity<EmployeeDTO> addEmployee(
            Department department,
            @Valid @RequestBody CreateEmployeeDTO editEmployeeDTO
    ){



        return ResponseEntity.status(201).body(EmployeeDTO.of(employeeService.addEmployee( editEmployeeDTO)));


    }


    @Operation(summary = "List employees", description = "Returns every employee. If there are none, it returns an empty list")
    @ApiResponse(responseCode = "200", description = "List of employees",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class)),
                    examples = @ExampleObject(name = "List", value = """
            [
              { "fullName": "Ana García López", "position": "Desarrolladora senior", "salary": 3500.00, "department": "Desarrollo" },
              { "fullName": "Luis Pérez Ruiz", "position": "Desarrollador", "salary": 3000.00, "department": "Desarrollo" }
            ]
            """)))
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAll(Employee e) {

        List<Employee> result = employeeService.getAllEmployees();

        return ResponseEntity.ok(
                result
                        .stream()
                        .map(EmployeeDTO :: of)
                        .toList());

    }

    @Operation(summary = "Get an employee", description = "Returns the employee with the given id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class),
                            examples = @ExampleObject(value = """
                { "fullName": "Ana García López", "position": "Desarrolladora senior", "salary": 3500.00, "department": "Desarrollo" }
                """))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                {
                  "type": "https://example.com/errors/not-found",
                  "title": "Recurso no encontrado",
                  "status": 404,
                  "detail": "Employee not found with id 99",
                  "instance": "/api/v1/employee/99"
                }
                """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById( @PathVariable @Min(value = 1) Long id ){


        return ResponseEntity.ok(

                EmployeeDTO.of(employeeService.getById(id))

        );



    }

    @Operation(summary = "Update an employee", description = "Updates the employee with the given id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Employee or department not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateDepartment(

            @PathVariable @Min(value = 1) Long id,
            @Valid @RequestBody CreateEmployeeDTO employeeDTO
    ){

        return ResponseEntity.status(200).body(

                EmployeeDTO.of(employeeService.updateEmployee(id, employeeDTO))

        );

    }

    @Operation(summary = "Delete an employee", description = "Deletes the employee with the given id")
    @ApiResponse(responseCode = "204", description = "Employee deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDTO> deleteEmployee(

            @PathVariable @Min(value = 1) Long id

    ){

        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();


    }


    @PutMapping({"/{employeeId}/department", "/{employeeId}/department/{departmentId}"})
    public ResponseEntity<EmployeeDTO> switchDepartment(

            @Min(value = 1)
            @PathVariable Long employeeId,
             @Min(value = 1)
             @PathVariable (required = false) Long departmentId

    ){

        return ResponseEntity.status(201).body(EmployeeDTO.of(employeeService.switchDepartment(employeeId , departmentId)));




    }






}
