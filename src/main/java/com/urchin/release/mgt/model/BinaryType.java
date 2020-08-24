package com.urchin.release.mgt.model;

public enum BinaryType {
    LINUX_TAR("tar.bz2"),
    LINUX_SNAP("snap"),
    WINDOWS_MSI("msi");

    private final String extension;

    BinaryType(String extension){
        this.extension = extension;
    }

    public String getExtension(){
        return extension;
    }
}
