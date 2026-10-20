package com.salesianos.registro_jornadas_api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.Builder;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.util.List;

@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    private final ProjectInfoProperties projectInfoProperties;

    public GlobalExceptionHandler(ProjectInfoProperties projectInfoProperties) {
        this.projectInfoProperties = projectInfoProperties;
    }

    //El handler que maneja la excion de DepartmentNotFound
    @ExceptionHandler
    public ProblemDetail handleDepartmentNotFound(EntityNotFoundException ex){

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Recurso no encontrado");

        //El texto viaja en todo momento  en la excepcion, es aqui donde se muestra
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/not-found"));

        return problem;

    }

    @ExceptionHandler
    public  ProblemDetail handleDuplicatedClocking(EntityNotFoundException ex){

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Recurso no encontrado");

        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/not-found"));

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
