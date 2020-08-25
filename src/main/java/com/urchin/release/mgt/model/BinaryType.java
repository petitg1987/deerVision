package com.urchin.release.mgt.model;

public enum BinaryType {
    LINUX_TAR("tar.bz2"),
    LINUX_DEB("deb"),
    WINDOWS_ZIP("zip"),
    WINDOWS_MSI("msi");

    private final String extension;

    BinaryType(String extension){
        this.extension = extension;
    }

    public String getExtension(){
        return extension;
    }
}
