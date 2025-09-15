package com.example.demo.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.example.demo.exception.NotFoundException;
import generated.openapi.model.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<Error> handleNotFoundException(NotFoundException exception) {
        var error = new Error()
                .entity(exception.getEntity())
                .message(exception.getMessage())
                .property(exception.getProperty())
                .invalidValue(exception.getInvalidValue());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }
}
