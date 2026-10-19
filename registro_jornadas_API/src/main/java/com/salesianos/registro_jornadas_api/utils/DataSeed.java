package com.salesianos.registro_jornadas_api.utils;

import com.salesianos.registro_jornadas_api.model.Clocking;
import com.salesianos.registro_jornadas_api.model.Department;
import com.salesianos.registro_jornadas_api.model.Employee;
import com.salesianos.registro_jornadas_api.model.Type;
import com.salesianos.registro_jornadas_api.repository.ClockingRepository;
import com.salesianos.registro_jornadas_api.repository.DepartmentRepository;
import com.salesianos.registro_jornadas_api.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataSeed {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ClockingRepository clockingRepository;

    public DataSeed(DepartmentRepository departmentRepository,
                    EmployeeRepository employeeRepository,
                    ClockingRepository clockingRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.clockingRepository = clockingRepository;
    }

    @PostConstruct
    public void initData() {

        // 1) Departamentos: no dependen de nadie, van primero.
        Department desarrollo = createDepartment("Desarrollo", "10000.00");
        Department rrhh = createDepartment("Recursos Humanos", "6000.00");
        Department marketing = createDepartment("Marketing", "5000.00");

        // 2) Empleados: necesitan que su departamento ya esté guardado.
        Employee ana = createEmployee("Ana García López", "Desarrolladora senior", "3500.00", desarrollo);
        Employee luis = createEmployee("Luis Pérez Ruiz", "Desarrollador", "3000.00", desarrollo);
        Employee marta = createEmployee("Marta Sánchez Díaz", "Analista", "3000.00", desarrollo);
        Employee carlos = createEmployee("Carlos Gómez Mena", "Responsable de personal", "2800.00", rrhh);
        Employee lucia = createEmployee("Lucía Torres Vega", "Técnica de selección", "2500.00", rrhh);
        Employee pablo = createEmployee("Pablo Ruiz Cano", "Community manager", "2200.00", null);

        // 3) Fichajes: necesitan que su empleado ya esté guardado.
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);
        LocalDate anteayer = hoy.minusDays(2);

        // Ana: dos jornadas completas. Su último fichaje es SALIDA, así que el siguiente será ENTRADA.
        clock(ana, anteayer, "08:00", Type.ENTRADA);
        clock(ana, anteayer, "16:05", Type.SALIDA);
        clock(ana, ayer, "07:55", Type.ENTRADA);
        clock(ana, ayer, "15:30", Type.SALIDA);

        // Luis: está dentro ahora mismo. Su último fichaje es ENTRADA, así que el siguiente será SALIDA.
        clock(luis, ayer, "09:00", Type.ENTRADA);
        clock(luis, ayer, "17:00", Type.SALIDA);
        clock(luis, hoy, "09:02", Type.ENTRADA);

        // Carlos: una jornada completa.
        clock(carlos, ayer, "08:30", Type.ENTRADA);
        clock(carlos, ayer, "14:30", Type.SALIDA);

        // Marta, Lucía y Pablo no tienen fichajes: su primer fichaje debe ser ENTRADA.
    }

    private Department createDepartment(String name, String budget) {
        Department d = new Department();
        d.setName(name);
        d.setBudget(new BigDecimal(budget));
        return departmentRepository.save(d);
    }

    private Employee createEmployee(String fullName, String position, String salary, Department department) {
        Employee e = new Employee();
        e.setFullName(fullName);
        e.setPosition(position);
        e.setSalary(new BigDecimal(salary));
        e.setDepartment(department); // null = empleado sin departamento
        return employeeRepository.save(e);
    }

    private void clock(Employee employee, LocalDate day, String time, Type type) {
        Clocking c = new Clocking();
        c.setEmployee(employee);
        c.setMoment(LocalDateTime.of(day, LocalTime.parse(time)));
        c.setType(type);
        clockingRepository.save(c);
    }
}
