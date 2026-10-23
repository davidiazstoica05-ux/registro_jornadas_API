# Tempus · API REST de registro de jornada

Tempus es una API REST hecha con **Spring Boot** para gestionar el registro de jornada laboral de una empresa: departamentos, empleados y fichajes de entrada y salida.

Es el proyecto de la unidad _API REST con Spring Boot_ de 2º DAM (Acceso a Datos y Programación de Servicios y Procesos), curso 2026-27, Salesianos de Triana.

---

## Índice

1. [Tecnologías](#tecnologías)
2. [Cómo arrancar el proyecto](#cómo-arrancar-el-proyecto)
3. [Documentación con Swagger](#documentación-con-swagger)
4. [Modelo de datos](#modelo-de-datos)
5. [Endpoints](#endpoints)
6. [Reglas de negocio](#reglas-de-negocio)
7. [Validación y gestión de errores](#validación-y-gestión-de-errores)
8. [Decisiones de diseño](#decisiones-de-diseño)
9. [Estructura del proyecto](#estructura-del-proyecto)
10. [Cómo probar la API](#cómo-probar-la-api)
11. [Limitaciones conocidas](#limitaciones-conocidas)
12. [Metodología de trabajo](#metodología-de-trabajo)

---

## Tecnologías

| Tecnología                  | Uso                                                        |
| --------------------------- | ---------------------------------------------------------- |
| Java 21                     | Lenguaje                                                   |
| Spring Boot 4.1 (Web MVC)   | API REST                                                   |
| Spring Data JPA + Hibernate | Persistencia                                               |
| H2                          | Base de datos en fichero (`./db/basedatos`)                |
| Jakarta Bean Validation     | Validación de entrada y validadores propios                |
| springdoc-openapi 3.1       | Documentación OpenAPI 3 + Swagger UI                       |
| Lombok                      | Menos código repetitivo (builders, getters, constructores) |
| Maven (wrapper incluido)    | Construcción del proyecto                                  |

---

## Cómo arrancar el proyecto

**Requisitos:** JDK 21. No hace falta instalar Maven, porque el proyecto incluye el wrapper.

```bash
git clone https://github.com/davidiazstoica05-ux/registro_jornadas_API.git
cd registro_jornadas_API/registro_jornadas_API
./mvnw spring-boot:run        # en Windows: mvnw.cmd spring-boot:run
```

La API arranca en `http://localhost:8080`.

También se puede abrir la carpeta `registro_jornadas_API` en IntelliJ y ejecutar `RegistroJornadasApiApplication`.

### Datos de ejemplo

Al arrancar, la clase `DataSeed` carga estos datos para poder probar sin crear nada:

| Departamento         | Presupuesto | Empleados                                |
| -------------------- | ----------- | ---------------------------------------- |
| 1 · Desarrollo       | 10.000,00   | Ana (3.500), Luis (3.000), Marta (3.000) |
| 2 · Recursos Humanos | 6.000,00    | Carlos (2.800), Lucía (2.500)            |
| 3 · Marketing        | 5.000,00    | —                                        |
| _(sin departamento)_ | —           | Pablo (2.200)                            |

Ana tiene dos jornadas completas, así que su último fichaje es una salida. Luis está dentro ahora mismo, así que su último fichaje es una entrada. El resto no tiene fichajes.

La base de datos se crea y se borra en cada arranque (`ddl-auto=create-drop`), así que siempre se empieza con estos datos.

### Consola H2

En `http://localhost:8080/h2-console` puedes ver las tablas.

- **JDBC URL:** `jdbc:h2:./db/basedatos`
- **Usuario:** `sa`
- **Contraseña:** vacía

---

## Documentación con Swagger

| Recurso         | URL                                         |
| --------------- | ------------------------------------------- |
| Swagger UI      | http://localhost:8080/swagger-ui/index.html |
| OpenAPI en JSON | http://localhost:8080/v3/api-docs           |

Todos los endpoints están documentados con `@Operation` y `@ApiResponses`, con ejemplos de las respuestas correctas y de los errores (`ProblemDetail`). Los DTOs están documentados con `@Schema` y tienen valores de ejemplo.

---

## Modelo de datos

```
Department 1 ──── * Employee 1 ──── * Clocking
```

| Entidad        | Campos                                                      | Relación                               |
| -------------- | ----------------------------------------------------------- | -------------------------------------- |
| **Department** | `idDepartment`, `name` (único), `budget` (BigDecimal)       | —                                      |
| **Employee**   | `idEmpleado`, `fullName`, `position`, `salary` (BigDecimal) | `@ManyToOne` con Department (opcional) |
| **Clocking**   | `id`, `moment` (LocalDateTime), `type` (`ENTRY` / `EXIT`)   | `@ManyToOne` con Employee              |

---

## Endpoints

URL base: `http://localhost:8080/api/v1`

### Departamentos · `/department`

| Método | Ruta               | Descripción                                                     | Respuestas                               |
| ------ | ------------------ | --------------------------------------------------------------- | ---------------------------------------- |
| GET    | `/department`      | Lista todos los departamentos                                   | 200 (lista vacía `[]` si no hay ninguno) |
| GET    | `/department/{id}` | Obtiene un departamento                                         | 200, 400, 404                            |
| POST   | `/department`      | Crea un departamento                                            | 201, 400                                 |
| PUT    | `/department/{id}` | Edita el nombre y el presupuesto                                | 200, 400, 404                            |
| DELETE | `/department/{id}` | Borra el departamento. Sus empleados se quedan sin departamento | 204, 400, 404                            |

Ejemplo de body para POST y PUT:

```json
{ "name": "Ventas", "budget": 8000.0 }
```

### Empleados · `/employee`

| Método | Ruta             | Descripción                                    | Respuestas    |
| ------ | ---------------- | ---------------------------------------------- | ------------- |
| GET    | `/employee`      | Lista todos los empleados                      | 200           |
| GET    | `/employee/{id}` | Obtiene un empleado                            | 200, 400, 404 |
| POST   | `/employee`      | Crea un empleado (el departamento es opcional) | 201, 400, 404 |
| PUT    | `/employee/{id}` | Edita un empleado                              | 200, 400, 404 |
| DELETE | `/employee/{id}` | Borra el empleado y sus fichajes               | 204, 400, 404 |

Ejemplo de body para POST:

```json
{
  "fullName": "Ana García López",
  "position": "Desarrolladora senior",
  "salary": 3500.0,
  "departmentId": 1
}
```

En el **PUT** hay que enviar también `employeeID` con el mismo id de la ruta. Lo explico en [Limitaciones conocidas](#limitaciones-conocidas).

```json
{
  "employeeID": 1,
  "fullName": "Ana García López",
  "position": "Desarrolladora senior",
  "salary": 3600.0,
  "departmentId": 1
}
```

La respuesta incluye el **nombre** del departamento, o `"Sin departamento"` si no tiene:

```json
{
  "fullName": "Ana García López",
  "position": "Desarrolladora senior",
  "salary": 3600.0,
  "department": "Desarrollo"
}
```

### Asignar un empleado a un departamento

| Método | Ruta                                               | Descripción                        | Respuestas         |
| ------ | -------------------------------------------------- | ---------------------------------- | ------------------ |
| PUT    | `/employee/{employeeId}/department/{departmentId}` | Asigna el empleado al departamento | 200, 400, 404, 409 |
| PUT    | `/employee/{employeeId}/department`                | Quita el departamento al empleado  | 200, 400, 404      |

### Fichajes · `/employee/{employeeId}/clockings`

| Método | Ruta                                          | Descripción                                                                  | Respuestas                       |
| ------ | --------------------------------------------- | ---------------------------------------------------------------------------- | -------------------------------- |
| POST   | `/employee/{employeeId}/clockings`            | Registra un fichaje con la hora actual. Alterna ENTRY y EXIT automáticamente | 201, 400, 404                    |
| POST   | `/employee/{employeeId}/clockings?type=ENTRY` | Fuerza el tipo del fichaje (`ENTRY` o `EXIT`)                                | 201, 400, 404, 409               |
| GET    | `/employee/{employeeId}/clockings`            | Historial de fichajes, el más reciente primero                               | 200 (`[]` si no tiene), 400, 404 |

Ejemplo de respuesta:

```json
{
  "moment": "2026-10-23T08:00:00",
  "type": "ENTRY",
  "nombreEmpleado": "Ana García López"
}
```

### Resumen de códigos HTTP

| Código          | Cuándo                                                                                                                               |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| 200 OK          | Consulta o edición correcta                                                                                                          |
| 201 Created     | Recurso creado (departamento, empleado o fichaje)                                                                                    |
| 204 No Content  | Borrado correcto                                                                                                                     |
| 400 Bad Request | Datos no válidos, id menor que 1, JSON mal formado, tipo de fichaje inexistente o presupuesto superado al crear o editar un empleado |
| 404 Not Found   | El departamento o el empleado no existen                                                                                             |
| 409 Conflict    | Fichaje duplicado, o presupuesto superado al cambiar a un empleado de departamento                                                   |

---

## Reglas de negocio

### Fichajes alternos

- Si no se envía `type`, el sistema mira el último fichaje del empleado. Si fue `ENTRY`, registra un `EXIT`, y al revés. El primer fichaje de un empleado siempre es `ENTRY`.
- Si se envía `type` y coincide con el del último fichaje, se lanza `DuplicatedClockingException` y se devuelve un **409 Conflict**.
- El momento del fichaje lo pone el servidor (`LocalDateTime.now()`), no el cliente, para que no se pueda falsear la hora.

### Presupuesto del departamento

La suma de los salarios de los empleados de un departamento no puede superar su presupuesto. Se comprueba en dos sitios:

1. **Al crear o editar un empleado** (`POST` y `PUT /employee`): lo comprueba la anotación propia `@BudgetNotExceed` en el DTO de entrada. Como es un error en los datos que envía el cliente, devuelve **400** con el mensaje en `invalid-params`.
2. **Al cambiar a un empleado de departamento** (`PUT /employee/{id}/department/{deptId}`): aquí no hay body que validar, así que lo comprueba el servicio y lanza `BudgetExceededException`, que devuelve **409 Conflict** porque choca con el estado actual del departamento.

La suma se calcula en la base de datos con una consulta JPQL (`SUM` + `COALESCE`). Al editar, la consulta excluye al propio empleado para no contar su sueldo dos veces.

### Borrados

- Al borrar un departamento, sus empleados no se borran: se quedan sin departamento.
- Al borrar un empleado, se borran también sus fichajes.

---

## Validación y gestión de errores

### Anotaciones de validación

| DTO                 | Campo      | Validaciones                                         |
| ------------------- | ---------- | ---------------------------------------------------- |
| `EditDepartmentDTO` | `name`     | `@NotBlank`, `@UniqueDepartmentName` (propia)        |
|                     | `budget`   | `@NotNull`, `@Positive`                              |
| `CreateEmployeeDTO` | clase      | `@BudgetNotExceed` (propia, de clase)                |
|                     | `fullName` | `@NotBlank`, `@Pattern` (al menos nombre y apellido) |
|                     | `position` | `@NotBlank`                                          |
|                     | `salary`   | `@NotNull`, `@Positive`                              |
| Ids en la ruta      | `{id}`     | `@Min(1)`                                            |

### Validadores propios

- **`@UniqueDepartmentName`**: comprueba en la base de datos que no exista otro departamento con ese nombre.
- **`@BudgetNotExceed`**: es una anotación de **clase** (`@Target(TYPE)`), porque necesita leer a la vez el salario y el `departmentId` del DTO. Suma los salarios del departamento y comprueba que el nuevo no supere el presupuesto.

### Mensajes externalizados

Los mensajes de validación están en `src/main/resources/messages/errors.properties` y se referencian desde las anotaciones con claves (`message = "{employee.salary.positive}"`). La clase `ValidationConfig` registra un `MessageSource` y lo conecta al validador con `LocalValidatorFactoryBean`. Así, para traducir la API a otro idioma bastaría con añadir un `errors_en.properties`, sin tocar el código.

### Excepciones propias

| Excepción                     | Código |
| ----------------------------- | ------ |
| `DepartmentNotFoundException` | 404    |
| `EmployeeNotFoundException`   | 404    |
| `DuplicatedClockingException` | 409    |
| `BudgetExceededException`     | 409    |

### Gestión unificada

`GlobalExceptionHandler` (`@RestControllerAdvice` que hereda de `ResponseEntityExceptionHandler`) convierte todas las excepciones en respuestas **`ProblemDetail`** (RFC 7807). Los errores de validación incluyen la lista `invalid-params` con el campo, el mensaje y el valor rechazado:

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation Error",
  "instance": "/api/v1/employee",
  "invalid-params": [
    {
      "object": "createEmployeeDTO",
      "message": "The salary must be positive",
      "field": "salary",
      "rejectedValue": -100
    }
  ]
}
```

---

## Decisiones de diseño

- **Uso obligatorio de DTOs.** Las entidades nunca salen del servicio hacia el cliente. Hay DTOs de **entrada** (`EditDepartmentDTO`, `CreateEmployeeDTO`), que reciben ids y llevan las validaciones, y DTOs de **salida** (`DepartmentDTO`, `EmployeeDTO`, `ClockDTO`), que muestran solo lo que interesa, como el nombre del departamento en lugar del objeto completo. Los métodos `to()` convierten de DTO a entidad y `of()` de entidad a DTO. Así se evitan los bucles infinitos al serializar a JSON.
- **Relaciones unidireccionales.** Solo existe `@ManyToOne` en `Employee` y en `Clocking`. No hace falta navegar desde `Department` hacia sus empleados, porque la suma de salarios se hace con una consulta en la base de datos, sin cargar listas en memoria.
- **`BigDecimal` para el dinero.** Evita los errores de redondeo de `double`. Las comparaciones se hacen con `compareTo` y las sumas con `add`.
- **Departamento opcional.** Un empleado puede no tener departamento (`departmentId` a `null`).
- **Optional + `orElseThrow`.** Cuando un recurso no existe, el servicio lanza la excepción `NotFound` correspondiente, que el handler traduce a 404 en lugar de un 500.

---

## Estructura del proyecto

```
src/main/java/com/salesianos/registro_jornadas_api
├── configuration   ValidationConfig (MessageSource + validador)
├── controller      DepartmentController, EmployeeController, ClockingController
├── dto             DTOs de entrada y salida (department, employee, clocking)
├── error           Excepciones propias y GlobalExceptionHandler
├── model           Entidades JPA y enum Type
├── repository      Repositorios Spring Data (con consultas JPQL)
├── service         Lógica de negocio
├── utils           DataSeed (datos de ejemplo)
└── validation      Anotaciones y validadores propios
src/main/resources
├── application.properties
├── messages/errors.properties
└── *.http          Peticiones de prueba
```

---

## Cómo probar la API

En `src/main/resources` hay cuatro ficheros `.http` con peticiones de prueba, tanto de casos correctos como de errores:

| Fichero                    | Qué prueba                                            |
| -------------------------- | ----------------------------------------------------- |
| `DepartmentsRequests.http` | CRUD de departamentos                                 |
| `EmployeesRequests.http`   | CRUD de empleados                                     |
| `AssignRequests.http`      | Asignar y quitar departamento, y presupuesto superado |
| `ClockRequests.http`       | Fichajes, alternancia, 409 e historial                |

Se ejecutan directamente desde IntelliJ (cliente HTTP integrado) con la aplicación arrancada. Cada petición indica en su comentario el código de respuesta que se espera. Con el profesor se acordó usar estos ficheros en lugar de la colección de Postman.

También se puede probar todo desde Swagger UI.

---

## Limitaciones conocidas

- **PUT de empleado y `employeeID`.** El validador `@BudgetNotExceed` necesita saber qué empleado se está editando para no contar su sueldo antiguo. Como el validador solo ve el body, el id debe ir también en `employeeID`. Si no se envía, el sueldo antiguo se suma dos veces y puede dar un 400 falso aunque el presupuesto no se supere.
- **PUT de departamento con el mismo nombre.** `@UniqueDepartmentName` solo ve el nombre, no el id del departamento que se edita. Por eso, editar un departamento manteniendo su nombre (por ejemplo, para cambiar solo el presupuesto) devuelve 400 "nombre repetido". Para arreglarlo habría que convertirla en una validación de clase que reciba también el id, igual que `@BudgetNotExceed`.
- **Presupuesto superado: 400 o 409.** Es una decisión, no un error. Se explica en [Reglas de negocio](#presupuesto-del-departamento).
- **Mensajes de las excepciones.** Los mensajes de validación están externalizados en `errors.properties`. Los textos de las excepciones de negocio se construyen en sus constructores.

---

## Metodología de trabajo

El proyecto se ha desarrollado por **historias de usuario**: una rama `feature/HU-XX` por historia, fusionada en `develop` mediante pull request, más ramas `fix/...` para las correcciones. Al terminar, `develop` se fusiona en `main`.

---

**Autor:** David Díaz Stoica · 2º DAM · Salesianos de Triana (Sevilla)
