package com.salesianos.registro_jornadas_api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianos.registro_jornadas_api.error.Duplicated.DuplicatedClockingException;
import com.salesianos.registro_jornadas_api.error.notfound.DepartmentNotFoundException;
import com.salesianos.registro_jornadas_api.error.notfound.EmployeeNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //El handler que maneja la excion de DepartmentNotFound
    @ExceptionHandler
    public ProblemDetail handleDepartmentNotFound(DepartmentNotFoundException ex){

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resource not found");

        //El texto viaja en todo momento  en la excepcion, es aqui donde se muestra
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/not-found"));

        return problem;

    }

    @ExceptionHandler
    public  ProblemDetail handleDuplicatedClocking(DuplicatedClockingException ex){

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("The clocking can not be duplicated");

        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/not-found"));

        return problem;

    }

    @ExceptionHandler
    public  ProblemDetail handleEmployeeNotFoundException(EmployeeNotFoundException ex){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resource not found");

        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/not-found"));

        return problem;

    }

    @ExceptionHandler
    public ProblemDetail handleBudgetExceeded(BudgetExceededException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Budget exceeded");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/budget-exceeded"));

        return problem;
    }


    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation Error");

        List<ApiValidationSubError> subErrors =
                ex.getAllErrors().stream()
                        .map(ApiValidationSubError::from)
                        .toList();

        result.setProperty("invalid-params", subErrors);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(result);
    }


    protected @Nullable ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ProblemDetail result = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation Error");

        List<ApiValidationSubError> subErrors =
                ex.getAllErrors().stream()
                        .map(error -> error instanceof ObjectError objectError
                                ? ApiValidationSubError.from(objectError)
                                : new ApiValidationSubError("path", error.getDefaultMessage()))
                        .toList();

        result.setProperty("invalid-params", subErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(result);
    }

    @Builder
    record ApiValidationSubError(

            String object,
            String message,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            String field,
            @JsonInclude(JsonInclude.Include.NON_NULL)
            Object rejectedValue

    ){


    public ApiValidationSubError(String object, String message){


        this(object,message,null,null);

    }

    public static ApiValidationSubError from (ObjectError error){

        ApiValidationSubError result = null;


        if (error instanceof FieldError fieldError){

            result = ApiValidationSubError.builder()
                    .object(fieldError.getObjectName())
                    .message(fieldError.getDefaultMessage())
                    .field(fieldError.getField())
                    .rejectedValue(fieldError.getRejectedValue())
                    .build();

        } else {
            result = ApiValidationSubError.builder()
                    .object(error.getObjectName())
                    .message(error.getDefaultMessage())
                    .build();
        }

        return  result;


    }
        public static ApiValidationSubError from(ConstraintViolation v) {
            return ApiValidationSubError.builder()
                    .message(v.getMessage())
                    .rejectedValue(v.getInvalidValue())
                    .object(v.getRootBeanClass().getSimpleName())
                    .field(getFieldName(v))
                    .build();
        }

    }

    private static String getFieldName(ConstraintViolation<?> v) {

        String field = null;

        for (Path.Node node : v.getPropertyPath()) {
            if (node.getKind() == ElementKind.PROPERTY || node.getKind() == ElementKind.PARAMETER) {
                field = node.getName() != null ? node.getName() : node.toString();
            }
        }
        return field;
    }





}
