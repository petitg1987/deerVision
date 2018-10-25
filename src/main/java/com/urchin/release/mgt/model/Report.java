package com.urchin.release.mgt.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report {

    private static final int MAX_SAMPLE_VALUE_SIZE = 150;

    private LocalDateTime dateTime;
    private Path path;

    public Report(LocalDateTime dateTime, Path path) {
        this.dateTime = dateTime;
        this.path = path;
    }

    public String getDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH':'mm':'ss");
        return dateTime.format(dateTimeFormatter);
    }

    public String getSampleValue() {

        try {
            String value = new String(Files.readAllBytes(path));
            int maxLength = (value.length() < MAX_SAMPLE_VALUE_SIZE) ? value.length() : MAX_SAMPLE_VALUE_SIZE;
            return value.substring(0, maxLength);
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to read file: " + path.getFileName());
        }
    }

    public String getFileName() {
        return path.getFileName().toString();
    }

    public String getFilePath() {
        return path.toAbsolutePath().toString();
    }

}
