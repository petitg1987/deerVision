package studio.deervision.service;

import studio.deervision.config.properties.AppProperties;
import studio.deervision.exception.ApplicationException;
import studio.deervision.model.OperatingSystem;
import studio.deervision.model.Usage;
import studio.deervision.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UsageService {

    private final UsageRepository usageRepository;
    private final AppProperties appProperties;

    @Autowired
    public UsageService(UsageRepository usageRepository, AppProperties appProperties) {
        this.usageRepository = usageRepository;
        this.appProperties = appProperties;
    }

    public void registerNewUsage(String requestKey, String appId, String appVersion, OperatingSystem operatingSystem) throws ApplicationException {
        if (!Pattern.matches(appProperties.getVersionPattern(), appVersion)) {
            throw new ApplicationException("Invalid application version: " + appVersion);
        }
        usageRepository.saveAndFlush(new Usage(requestKey, appId, appVersion, operatingSystem));
    }

    public List<String> findDistinctAppId() {
        return usageRepository.findDistinctAppId();
    }

    public Map<LocalDate, Long> findUsagesBetweenDates(String appId, LocalDate startDate, LocalDate endDate, boolean ignoreSnapshot) {
        List<Usage> usages = usageRepository.findBetweenDates(appId, toStartDateTime(startDate), toEndDateTime(endDate), ignoreSnapshot);
        return usages.stream().collect(Collectors.groupingBy(bva -> bva.getDateTime().toLocalDate(), Collectors.counting()));
    }

    private LocalDateTime toStartDateTime(LocalDate startDate){
        return startDate.atTime(LocalTime.MIN);
    }

    private LocalDateTime toEndDateTime(LocalDate endDate){
        return endDate.atTime(LocalTime.MAX);
    }
}
