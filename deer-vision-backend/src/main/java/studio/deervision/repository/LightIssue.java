package studio.deervision.repository;

import studio.deervision.model.OperatingSystem;

import java.time.LocalDateTime;

public interface LightIssue {
    Long getId();
    String getSystemKey();
    String getAppId();
    String getAppVersion();
    OperatingSystem getOperatingSystem();
    LocalDateTime getDateTime();
}
