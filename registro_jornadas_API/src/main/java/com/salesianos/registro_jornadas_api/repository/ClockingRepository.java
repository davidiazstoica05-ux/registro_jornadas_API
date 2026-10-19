package com.salesianos.registro_jornadas_api.repository;

import com.salesianos.registro_jornadas_api.model.Clocking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClockingRepository extends JpaRepository<Clocking, Long> {
}
