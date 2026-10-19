package com.salesianos.registro_jornadas_api.controller;

import com.salesianos.registro_jornadas_api.dto.DepartmentListDTO;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tempus/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping
    public ResponseEntity<List<DepartmentListDTO>> getAll(Department d) {

        List<Department> result = departmentService.getAllDepartments();

        return ResponseEntity.ok(
                result
                        .stream()
                        .map(DepartmentListDTO :: of)
                        .toList());


    }



}
