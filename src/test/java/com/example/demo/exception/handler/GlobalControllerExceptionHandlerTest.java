package com.example.demo.exception.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.MemberEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GlobalControllerExceptionHandlerTest {

    @InjectMocks
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    @Test
    void handleNotFoundException() {
        // given
        var exception = new NotFoundException(
                "Could not find record.",
                MemberEntity.class.getSimpleName(),
                "id",
                String.valueOf(1));

        // when
        var result = globalControllerExceptionHandler.handleNotFoundException(exception);

        // then
        var expected = new ResponseEntity<>(
                new generated.openapi.model.Error()
                        .entity("MemberEntity")
                        .message("Could not find record.")
                        .property("id")
                        .invalidValue("1"),
                BAD_REQUEST);
        assertEquals(expected, result);
    }
}