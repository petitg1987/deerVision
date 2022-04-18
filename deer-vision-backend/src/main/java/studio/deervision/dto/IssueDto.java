package studio.deervision.dto;

import studio.deervision.model.Issue;

import java.time.format.DateTimeFormatter;

public class IssueDto {

    private final Long id;
    private final String value;
    private final String requestKey;
    private final String appId;
    private final String appVersion;
    private final String dateTime;
    private final String operatingSystem;

    public IssueDto(Issue issue) {
        this.id = issue.getId();
        this.value = issue.getValue();
        this.requestKey = issue.getRequestKey();
        this.appId = issue.getAppId();
        this.appVersion = issue.getAppVersion();
        this.dateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH':'mm':'ss").format(issue.getDateTime());
        this.operatingSystem = issue.getOperatingSystem().toOperatingSystemString();
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getDateTime() {
        return dateTime;
    }
}

