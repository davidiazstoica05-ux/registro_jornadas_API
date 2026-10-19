package com.salesianos.registro_jornadas_api.repository;

import com.salesianos.registro_jornadas_api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
