package studio.deervision.dto;

import studio.deervision.repository.LightIssue;

import java.time.format.DateTimeFormatter;

public class IssueListDto {

    private final Long id;
    private final String systemKey;
    private final String appId;
    private final String appVersion;
    private final String dateTime;
    private final String operatingSystem;

    public IssueListDto(LightIssue lightIssue) {
        this.id = lightIssue.getId();
        this.systemKey = lightIssue.getSystemKey();
        this.appId = lightIssue.getAppId();
        this.appVersion = lightIssue.getAppVersion();
        this.dateTime = DateTimeFormatter.ofPattern("dd/MM/yy HH':'mm':'ss").format(lightIssue.getDateTime());
        this.operatingSystem = lightIssue.getOperatingSystem().toOperatingSystemString();
    }

    public Long getId() {
        return id;
    }

    public String getSystemKey() {
        return systemKey;
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
