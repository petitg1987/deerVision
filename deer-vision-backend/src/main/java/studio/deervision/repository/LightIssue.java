package studio.deervision.repository;

import studio.deervision.model.OperatingSystem;

import java.time.LocalDateTime;

public interface LightIssue {
    Long getId();
    String getUserKey();
    String getAppId();
    String getAppVersion();
    OperatingSystem getOperatingSystem();
    LocalDateTime getDateTime();
}
