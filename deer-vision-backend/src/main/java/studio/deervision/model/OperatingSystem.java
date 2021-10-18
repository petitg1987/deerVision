package studio.deervision.model;

import com.google.common.base.CaseFormat;

public enum OperatingSystem {
    LINUX,
    WINDOWS,
    MAC_OS;

    public static OperatingSystem toOperatingSystem(String osString){
        return OperatingSystem.valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, osString));
    }

    public String toOperatingSystemString(){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, this.name());
    }
}
