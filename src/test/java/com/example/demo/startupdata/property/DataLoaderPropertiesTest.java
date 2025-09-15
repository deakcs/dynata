package com.example.demo.startupdata.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataLoaderPropertiesTest {

    @Autowired
    private DataLoaderProperties dataLoaderProperties;

    private static Stream<Arguments> input_getFileLocation_ok() {
        return Stream.of(
                arguments("Statuses.csv", "static/initData/OO - 2 - Statuses.csv"),
                arguments("Members.csv", "static/initData/OO - 2 - Members.csv"),
                arguments("Surveys.csv", "static/initData/OO - 2 - Surveys.csv"),
                arguments("Participation.csv", "static/initData/OO - 2 - Participation.csv")
        );
    }

    @ParameterizedTest
    @MethodSource("input_getFileLocation_ok")
    void getFileLocation_ok(String fileName, String expected) {
        // given

        // when
        var result = dataLoaderProperties.getFileLocation(fileName);

        // then
        assertEquals(expected, result);
    }

    @Test
    void getFileLocation_exception() {
        // given
        var fileName = "invalid";

        // when
        var result = assertThrows(RuntimeException.class, () -> dataLoaderProperties.getFileLocation(fileName));

        // then
        var expected = new RuntimeException("Could not get file path from fileName: " + fileName);
        assertEquals(expected.getMessage(), result.getMessage());
    }
}