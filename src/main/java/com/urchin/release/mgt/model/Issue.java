package com.urchin.release.mgt.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Issue {

    private static final int MAX_SAMPLE_VALUE_SIZE = 150;

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private String value;

    private String appVersion;

    private LocalDateTime dateTime;

    public Issue(){
    }

    public Issue(String value, String appVersion) {
        this.value = value;
        this.appVersion = appVersion;
        this.dateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTimeDisplay() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH':'mm':'ss");
        return dateTime.format(dateTimeFormatter);
    }

    public String getSampleValue() {
        int maxLength = Math.min(value.length(), MAX_SAMPLE_VALUE_SIZE);
        return value.substring(0, maxLength);
    }



}
