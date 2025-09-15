package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DemoApplicationTest {

    @InjectMocks
    private DemoApplication demoApplication;

    @Test
    void test_main() {
        // given
        final var args = new String[]{"1", "2", "3"};

        // when
        assertDoesNotThrow(() -> DemoApplication.main(args));

        // then
    }
}