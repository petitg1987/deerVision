package com.urchin.release.mgt.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class BinaryDownloadAudit {

    @Id
    @GeneratedValue
    private Long id;

    private String appVersion;

    private BinaryType binaryType;

    private LocalDate date;

    public BinaryDownloadAudit(){
    }

    public BinaryDownloadAudit(String appVersion, BinaryType binaryType, LocalDate date) {
        this.appVersion = appVersion;
        this.binaryType = binaryType;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
