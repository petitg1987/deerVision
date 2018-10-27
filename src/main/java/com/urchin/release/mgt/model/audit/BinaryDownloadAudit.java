package com.urchin.release.mgt.model.audit;

import com.urchin.release.mgt.model.BinaryType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BinaryDownloadAudit {

    @Id
    @GeneratedValue
    private Long id;

    private String appVersion;

    private BinaryType binaryType;

    private LocalDateTime dateTime;

    public BinaryDownloadAudit(){
    }

    public BinaryDownloadAudit(String appVersion, BinaryType binaryType) {
        this.appVersion = appVersion;
        this.binaryType = binaryType;
        this.dateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public BinaryType getBinaryType() {
        return binaryType;
    }

    public void setBinaryType(BinaryType binaryType) {
        this.binaryType = binaryType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
