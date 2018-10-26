package com.urchin.release.mgt.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class BinaryVersionAudit {

    @Id
    @GeneratedValue
    private Long id;

    private String version;

    private BinaryType binaryType;

    private LocalDate date;

    public BinaryVersionAudit(){
    }

    public BinaryVersionAudit(String version, BinaryType binaryType, LocalDate date) {
        this.version = version;
        this.binaryType = binaryType;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
