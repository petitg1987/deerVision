package studio.deervision.model.pe;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(name = "index_levelId", columnList = "levelId"))
public class ActionCompletionTime {

    @Id
    @GeneratedValue
    private Long id;

    private String requestKey;

    private String appVersion;

    private Integer levelId;

    private String actionName;

    private Long completionTime;

    public ActionCompletionTime() {

    }

    public ActionCompletionTime(String requestKey, String appVersion, Integer levelId, String actionName, Long completionTime) {
        this.requestKey = requestKey;
        this.appVersion = appVersion;
        this.levelId = levelId;
        this.actionName = actionName;
        this.completionTime = completionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Long completionTime) {
        this.completionTime = completionTime;
    }
}
