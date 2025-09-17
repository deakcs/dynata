package com.example.demo.startupdata;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public final class ObjectLoader {

    static <T> List<T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema
                    .emptySchema()
                    .withHeader();
            File file = new ClassPathResource(fileName).getFile();
            MappingIterator<T> readValues =
                    new CsvMapper()
                            .readerFor(type)
                            .with(bootstrapSchema)
                            .readValues(file);

            return readValues.readAll();
        } catch (Exception e) {
            log.error("Error occurred while loading object list from file " + fileName, e);
            throw new RuntimeException("Could not load file: " + fileName);
        }
    }
}
