package com.urchin.release.mgt.model;

import com.google.common.base.CaseFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Binary {

    private final String url;

    private final String filename;

    private final long sizeInBytes;

    private final String version;

    private BinaryType binaryType;

    private final LocalDateTime lastModified;

    public Binary(String url, String filename, long sizeInBytes, String version, LocalDateTime lastModified) {
        this.url = url;
        this.filename = filename;
        this.sizeInBytes = sizeInBytes;
        this.version = version;
        this.binaryType = retrieveBinaryType();
        this.lastModified = lastModified;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public BinaryType getBinaryType() {
        return binaryType;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public String getBinaryId() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, binaryType.name());
    }

    public String getFileName(){
        return filename;
    }

    public String getFileSizeMB(){
        double sizeDouble = sizeInBytes / 1000.0 / 1000.0;
        return String.format("%.01f", sizeDouble);
    }

    public String getLastModifiedDisplay() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH':'mm':'ss");
        return lastModified.format(dateTimeFormatter);
    }

    private BinaryType retrieveBinaryType() {
        for(BinaryType binaryType : BinaryType.values()) {
            if(getFileName().endsWith(binaryType.getExtension())){
                this.binaryType = binaryType;
                break;
            }
        }

        if(binaryType==null) {
            throw new IllegalArgumentException("Error to determine binary type for binary: " + url);
        }

        return binaryType;
    }
}
