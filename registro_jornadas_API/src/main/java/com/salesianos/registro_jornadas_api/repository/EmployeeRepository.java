package com.salesianos.registro_jornadas_api.repository;

import com.salesianos.registro_jornadas_api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    List<Employee> getByDepartment_IdDepartment(Long id);

    @Query("""
        SELECT COALESCE(SUM(e.salary), 0)
        FROM Employee e
        WHERE e.department.idDepartment = :departmentId
        """)
    BigDecimal sumSalariesByDepartment(@Param("departmentId") Long departmentId);

}
