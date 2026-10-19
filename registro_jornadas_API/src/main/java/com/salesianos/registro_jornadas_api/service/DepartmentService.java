package com.salesianos.registro_jornadas_api.service;

import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;


    public List<Department> getAllDepartments(){

        List<Department> result = departmentRepository.findAll();

        if (result.isEmpty()){


            //throw new aqui excpciom

        }


        return result;

    }

}
