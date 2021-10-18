package studio.deervision.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Usage {

    @Id
    @GeneratedValue
    private Long id;

    private String userKey;

    private String appId;

    private String appVersion;

    private OperatingSystem operatingSystem;

    private LocalDateTime dateTime;

    public Usage(){
    }

    public Usage(String userKey, String appId, String appVersion, OperatingSystem operatingSystem) {
        this.userKey = userKey;
        this.appId = appId;
        this.appVersion = appVersion;
        this.operatingSystem = operatingSystem;
        this.dateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
