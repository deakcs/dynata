package com.example.demo.startupdata.property;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Data
public class DataLoaderProperties {

    private List<String> initDataList;

    public String getFileLocation(String fileName) {
        return initDataList
                .stream()
                .filter(f -> f.contains(fileName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not get file path from fileName: " + fileName));
    }
}
