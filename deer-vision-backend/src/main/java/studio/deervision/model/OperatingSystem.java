package studio.deervision.model;

import com.google.common.base.CaseFormat;

public enum OperatingSystem {
    LINUX,
    WINDOWS,
    MAC_OS;

    public static OperatingSystem retrieveOperatingSystem(String os){
        String osString = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, os);
        return OperatingSystem.valueOf(osString);
    }
}
