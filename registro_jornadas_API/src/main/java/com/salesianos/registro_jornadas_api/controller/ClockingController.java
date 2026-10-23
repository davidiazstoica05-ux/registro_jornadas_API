package com.salesianos.registro_jornadas_api.controller;

import com.salesianos.registro_jornadas_api.dto.clocking.ClockDTO;
import com.salesianos.registro_jornadas_api.model.Clocking;
import com.salesianos.registro_jornadas_api.model.Type;
import com.salesianos.registro_jornadas_api.service.ClockingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/{employeeId}/clockings")
@RequiredArgsConstructor
@Tag(name = "Clocking Controller", description = "A clocking's controller")

public class ClockingController {


    private final ClockingService clockingService;




    @Operation(summary = "Clock in or out",
            description = "Registers a clocking for the employee at the current moment. " +
                    "If no type is sent, it alternates automatically with the last one (the first one is ENTRY)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clocking registered",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClockDTO.class),
                            examples = @ExampleObject(value = """
                { "moment": "2026-10-23T08:00:00", "type": "ENTRY", "nombreEmpleado": "Ana García López" }
                """))),
            @ApiResponse(responseCode = "400", description = "Invalid id or invalid type (it must be ENTRY or EXIT)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "The type is the same as the last clocking",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(value = """
                {
                  "type": "https://example.com/errors/duplicated-clocking",
                  "title": "Duplicated clocking",
                  "status": 409,
                  "detail": "Employee 1 already has a ENTRY as last clocking",
                  "instance": "/api/v1/employee/1/clockings"
                }
                """)))
    })
    @PostMapping()
    public ResponseEntity<ClockDTO> clock(
            @PathVariable @Min(value = 1) Long employeeId,
            @RequestParam(required = false) Type type
    ) {
        return ResponseEntity.status(201)
                .body(ClockDTO.of(clockingService.addClock(employeeId, type)));
    }


    @Operation(summary = "List an employee's clockings", description = "Returns every clocking of the employee, " +
            "the most recent first. If there are none, it returns an empty list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of clockings",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClockDTO.class)),
                            examples = @ExampleObject(name = "List", value = """
                [
                  { "moment": "2026-10-23T15:00:00", "type": "EXIT", "nombreEmpleado": "Ana García López" },
                  { "moment": "2026-10-23T08:00:00", "type": "ENTRY", "nombreEmpleado": "Ana García López" }
                ]
                """))),
            @ApiResponse(responseCode = "400", description = "The id must be 1 or greater",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping()
    public ResponseEntity<List<ClockDTO>> getAllClocks(


            @Min(value = 1)
            @PathVariable long employeeId

    ) {


        List<Clocking> resul = clockingService.getAllClocks(employeeId);


        return ResponseEntity.ok(

                resul.stream()
                        .map(ClockDTO ::of)
                        .toList()
        );

    }






}
