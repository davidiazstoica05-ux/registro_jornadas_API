package com.salesianos.registro_jornadas_api.controller;

import com.salesianos.registro_jornadas_api.dto.department.DepartmentDTO;
import com.salesianos.registro_jornadas_api.dto.department.EditDepartmentDTO;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.service.DepartmentService;
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
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
@Tag(name = "Department Controller", description = "A department's controller")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "List departments", description = "Returns every department. If there are none, it returns an empty list")
    @ApiResponse(responseCode = "200", description = "List of departments",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DepartmentDTO.class)),
                    examples = @ExampleObject(name = "List", value = """
            [
              { "name": "Desarrollo", "budget": 10000.00 },
              { "name": "Marketing", "budget": 6000.00 }
            ]
            """)))
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAll() {

        List<Department> result = departmentService.getAllDepartments();

        return ResponseEntity.ok(
                result
                        .stream()
                        .map(DepartmentDTO :: of)
                        .toList());


    }

    @Operation(summary = "Get a department", description = "Returns the department with the given id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentDTO.class),
                            examples = @ExampleObject(value = """
                { "name": "Desarrollo", "budget": 10000.00 }
                """))),
            @ApiResponse(responseCode = "400", description = "The id must be 1 or greater",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                {
                  "type": "https://example.com/errors/not-found",
                  "title": "Recurso no encontrado",
                  "status": 404,
                  "detail": "Department not found with id 99",
                  "instance": "/api/v1/department/99"
                }
                """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable  @Min(value = 1) Long id){

        return ResponseEntity.ok(

                DepartmentDTO.of(departmentService.getById(id))
        );

    }

    @Operation(summary = "Create a department", description = "Creates a department. The name must be unique")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Department created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data or repeated name",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                {
                  "title": "Bad Request",
                  "status": 400,
                  "detail": "Validation Error",
                  "instance": "/api/v1/department",
                  "invalid-params": [
                    { "object": "editDepartmentDTO", "message": "The name is required", "field": "name", "rejectedValue": "" }
                  ]
                }
                """)))
    })
    @PostMapping
    public ResponseEntity<DepartmentDTO> addDepartment(

            @Valid @RequestBody EditDepartmentDTO departmentDTO

    ){

        return ResponseEntity.status(201)
                .body(DepartmentDTO.of(departmentService
                        .addDepartment(departmentDTO))
                );

    }

    @Operation(summary = "Edit a department", description = "Replaces the name and the budget of the given department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data, invalid id or repeated name",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(

            @PathVariable @Min(value = 1) Long id,
            @Valid @RequestBody EditDepartmentDTO department
    ){

        return ResponseEntity.ok(

                DepartmentDTO.of(departmentService.updateDepartment(id, department))

        );

    }

    @Operation(summary = "Delete a department", description = "Deletes the department with the given id. Its employees are left without department")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Department deleted"),
            @ApiResponse(responseCode = "400", description = "The id must be 1 or greater",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDepartment(

            @PathVariable @Min(value = 1) Long id

    ){


        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();



    }








}
