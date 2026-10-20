package com.salesianos.registro_jornadas_api.repository;

import com.salesianos.registro_jornadas_api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    List<Employee> getByDepartment_IdDepartment(Long id);

}
