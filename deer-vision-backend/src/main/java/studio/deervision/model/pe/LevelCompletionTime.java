package studio.deervision.model.pe;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(name = "index_levelId", columnList = "levelId"))
public class LevelCompletionTime {

    @Id
    @GeneratedValue
    private Long id;

    private String requestKey;

    private String appVersion;

    private Integer levelId;

    private Long completionTime;

    public LevelCompletionTime() {

    }

    public LevelCompletionTime(String requestKey, String appVersion, Integer levelId, Long completionTime) {
        this.requestKey = requestKey;
        this.appVersion = appVersion;
        this.levelId = levelId;
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

    public Long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Long completionTime) {
        this.completionTime = completionTime;
    }
}
