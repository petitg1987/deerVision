package studio.deervision.model;

import com.google.common.base.CaseFormat;

public enum IssueOrigin {
    CRASH,
    ERROR_LOGGED,
    USER_REPORT;

    public static IssueOrigin toIssueOrigin(String ioString){
        return IssueOrigin.valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, ioString));
    }

    public String toIssueOriginString(){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, this.name());
    }
}
