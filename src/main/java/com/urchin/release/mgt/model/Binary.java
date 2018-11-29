package com.urchin.release.mgt.model;

import com.google.common.base.CaseFormat;

public class Binary {

    private String url;

    private long sizeInBytes;

    private String version;

    private BinaryType binaryType;

    public Binary(String url, long sizeInBytes, String version) {
        this.url = url;
        this.sizeInBytes = sizeInBytes;
        this.version = version;

        this.binaryType = retrieveBinaryType();
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

    public String getBinaryId() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, binaryType.name());
    }

    public String getFileName(){
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public String getFileSizeMB(){
        double sizeDouble = sizeInBytes / 1000.0 / 1000.0;
        return String.format("%.01f", sizeDouble);
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
