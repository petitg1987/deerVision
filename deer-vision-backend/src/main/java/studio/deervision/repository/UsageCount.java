package studio.deervision.repository;

import java.time.LocalDate;

public interface UsageCount {
    LocalDate getUsageDate();
    Long getUsageCount();
}
