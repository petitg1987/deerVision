package com.urchin.release.mgt.model;

import com.google.common.base.CaseFormat;

import java.nio.file.Path;

public class Binary {

    private Path path;

    private String version;

    private BinaryType binaryType;

    public Binary(Path path, String version) {
        this.path = path;
        this.version = version;
        this.binaryType = retrieveBinaryType(path);
    }

    public Path getPath() {
        return path;
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
        return path.getFileName().toString();
    }

    public String getFileSizeMB(){
        double sizeDouble = path.toFile().length() / 1000.0 / 1000.0;
        return String.format("%.01f", sizeDouble);
    }

    private BinaryType retrieveBinaryType(Path path) {
        String filename = path.getFileName().toString();

        for(BinaryType binaryType : BinaryType.values()) {
            if(filename.endsWith(binaryType.getExtension())){
                this.binaryType = binaryType;
                break;
            }
        }

        if(binaryType==null) {
            throw new IllegalArgumentException("Error to determine binary type for filename: " + filename);
        }

        return binaryType;
    }
}
