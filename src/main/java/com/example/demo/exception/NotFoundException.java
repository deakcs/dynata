package com.example.demo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class NotFoundException extends RuntimeException {

    private final String entity;
    private final String property;
    private final String invalidValue;

    public NotFoundException(String message, String entity, String property, String invalidValue) {
        super(message);
        this.entity = entity;
        this.property = property;
        this.invalidValue = invalidValue;
    }
}
