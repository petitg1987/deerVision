package studio.deervision.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
public class Issue {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private String data;

    private IssueOrigin origin;

    private String requestKey;

    private String appId;

    private String appVersion;

    private OperatingSystem operatingSystem;

    private LocalDateTime dateTime;

    public Issue(){
    }

    public Issue(String data, IssueOrigin origin, String requestKey, String appId, String appVersion, OperatingSystem operatingSystem) {
        this.data = data;
        this.origin = origin;
        this.requestKey = requestKey;
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

    public String getData() {
        return data;
    }

    public void setData(String value) {
        this.data = value;
    }

    public IssueOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(IssueOrigin origin) {
        this.origin = origin;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
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
