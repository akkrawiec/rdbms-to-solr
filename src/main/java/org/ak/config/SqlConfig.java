package org.ak.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Configuration
@EnableAutoConfiguration
public class SqlConfig {
    private static final String MAIN_SELECT_FILE = "sql/location_select.sql";
    private static final String ELEVATION_SELECT_FILE = "sql/main_select.sql";
    private static final String LATITUDE_SELECT_FILE = "sql/main_select.sql";
    private static final String LONGITUDE_SELECT_FILE = "sql/main_select.sql";

    @Bean
    public String selectLocationSql() {
        return readFileContent(MAIN_SELECT_FILE);
    }

    @Bean
    public String selectElevationSql() {
        return readFileContent(ELEVATION_SELECT_FILE);
    }

    @Bean
    public String selectLatitudeSql() {
        return readFileContent(LATITUDE_SELECT_FILE);
    }

    @Bean
    public String selectLongitudeSql() {
        return readFileContent(LONGITUDE_SELECT_FILE);
    }

    private String readFileContent(String pathToFile) {
        final StringBuilder builder = new StringBuilder();
        Resource resource = new ClassPathResource(pathToFile);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            reader.lines().forEach(line -> {
                builder.append(line);
                builder.append(System.lineSeparator());
            });

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return builder.toString();
    }
}
