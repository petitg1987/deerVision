package com.urchin.release.mgt.model.audit;

import com.urchin.release.mgt.model.OperatingSystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BinaryVersionAudit {

    @Id
    @GeneratedValue
    private Long id;

    private String version;

    private String userKey;

    private OperatingSystem operatingSystem;

    private LocalDateTime dateTime;

    public BinaryVersionAudit(){
    }

    public BinaryVersionAudit(String version, String userKey, OperatingSystem operatingSystem) {
        this.version = version;
        this.userKey = userKey;
        this.operatingSystem = operatingSystem;
        this.dateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
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
