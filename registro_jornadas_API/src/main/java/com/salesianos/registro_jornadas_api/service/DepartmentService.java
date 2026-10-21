package com.salesianos.registro_jornadas_api.service;

import com.salesianos.registro_jornadas_api.dto.department.EditDepartmentDTO;
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
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public List<Department> getAllDepartments(){

        return departmentRepository.findAll();



    }



    public Department getById(Long id){

       return departmentRepository.findById(id)
               .orElseThrow( () -> new DepartmentNotFoundException(id));


    }


    public Department addDepartment(EditDepartmentDTO editDepartmentDTO){

    Department d = editDepartmentDTO.to();

    return departmentRepository.save(d);


    }


    public Department updateDepartment (Long id, EditDepartmentDTO editDepartmentDTO){



        return departmentRepository.findById(id)
                                    .map( d -> {
                                     d.setBudget(editDepartmentDTO.budget());
                                     d.setName(editDepartmentDTO.name());
                                    return departmentRepository.save(d);
                                    })
                .orElseThrow(() -> new DepartmentNotFoundException(id));


    }

    public void deleteProduct(Long id){



        if (departmentRepository.existsById(id)){

            List<Employee> employees = employeeRepository.getByDepartment_IdDepartment(id);

            employees.stream()
                    .forEach( e -> e.setDepartment(null));

            departmentRepository.deleteById(id);

        }else {

            throw new DepartmentNotFoundException(id);

        }


    }
}
