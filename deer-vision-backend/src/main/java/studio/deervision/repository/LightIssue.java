package studio.deervision.repository;

import studio.deervision.model.IssueOrigin;
import studio.deervision.model.OperatingSystem;

import java.time.LocalDateTime;

public interface LightIssue {
    Long getId();
    IssueOrigin getOrigin();
    String getRequestKey();
    String getAppId();
    String getAppVersion();
    OperatingSystem getOperatingSystem();
    LocalDateTime getDateTime();
}
