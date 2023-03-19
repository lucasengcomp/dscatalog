package com.devsuperior.dscatalog.resources.exeptions;

import com.devsuperior.dscatalog.services.Utils.exceptions.DataBaeException;
import com.devsuperior.dscatalog.services.Utils.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

import static com.devsuperior.dscatalog.resources.exeptions.Utils.*;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError();
        patternMessage(error, status, RESOURCE_NOT_FOUND, e.getMessage(), request);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataBaeException.class)
    public ResponseEntity<StandardError> database(DataBaeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError();
        patternMessage(error, status, DATABASE_EXCEPTION, e.getMessage(), request);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError error = new ValidationError();
        patternMessage(error, status, ERROR_UNPROCESABLE_ENTITY, e.getMessage(), request);

        fieldWithErrors(e, error);

        return ResponseEntity.status(status).body(error);
    }

    private static void fieldWithErrors(MethodArgumentNotValidException e, ValidationError error) {
        for (FieldError fieldError:  e.getBindingResult().getFieldErrors()) {
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    private void patternMessage(StandardError error, HttpStatus status, String databaseException, String e, HttpServletRequest request) {
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError(databaseException);
        error.setMessage(e);
        error.setPath(request.getRequestURI());
    }
}
