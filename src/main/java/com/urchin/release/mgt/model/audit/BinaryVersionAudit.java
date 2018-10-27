package com.urchin.release.mgt.model.audit;

import com.urchin.release.mgt.model.BinaryType;

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

    private BinaryType binaryType;

    private LocalDateTime dateTime;

    public BinaryVersionAudit(){
    }

    public BinaryVersionAudit(String version, BinaryType binaryType) {
        this.version = version;
        this.binaryType = binaryType;
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
