package com.salesianos.registro_jornadas_api.repository;

import com.salesianos.registro_jornadas_api.model.Clocking;
import com.salesianos.registro_jornadas_api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClockingRepository extends JpaRepository<Clocking, Long> {


    Optional<Clocking> findFirstByEmployeeIdEmpleadoOrderByMomentDesc(Long employeeId);



}
