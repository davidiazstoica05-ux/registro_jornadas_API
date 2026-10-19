package com.salesianos.registro_jornadas_api.repository;

import com.salesianos.registro_jornadas_api.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
