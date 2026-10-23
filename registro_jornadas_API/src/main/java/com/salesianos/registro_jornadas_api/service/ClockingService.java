package com.salesianos.registro_jornadas_api.service;

import com.salesianos.registro_jornadas_api.error.Duplicated.DuplicatedClockingException;
import com.salesianos.registro_jornadas_api.error.notfound.EmployeeNotFoundException;
import com.salesianos.registro_jornadas_api.model.Clocking;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.model.Type;
import com.salesianos.registro_jornadas_api.repository.ClockingRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClockingService {

    private final ClockingRepository clockingRepository;

    private final EmployeeRepository employeeRepository;

    public Clocking addClock(Long employeeID, Type type) {

        Employee employee = employeeRepository.findById(employeeID)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeID));

        Optional<Clocking> lastClocking = clockingRepository
                .findFirstByEmployeeIdEmpleadoOrderByMomentDesc(employeeID);

        Type lastType = null;

        if (lastClocking.isPresent()) {
            lastType = lastClocking.get().getType();
        }

        Type newType;

        if (type == null) {
            newType = switch (lastType) {
                case ENTRY -> Type.EXIT;
                case EXIT -> Type.ENTRY;
                case null -> Type.ENTRY;
            };
        } else if (type == lastType) {
            throw new DuplicatedClockingException(employeeID, type);
        } else {
            newType = type;
        }

        Clocking clocking = Clocking.builder()
                .employee(employee)
                .moment(LocalDateTime.now())
                .type(newType)
                .build();

        return clockingRepository.save(clocking);
    }


    public List<Clocking> getAllClocks(Long employeeID){


        employeeRepository.findById(employeeID).orElseThrow(() -> new EmployeeNotFoundException(employeeID));


      return clockingRepository.findAllByEmployeeIdEmpleadoOrderByMomentDesc(employeeID);








    }



}
