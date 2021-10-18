package studio.deervision.service;

import studio.deervision.config.properties.IssueProperties;
import studio.deervision.model.Issue;
import studio.deervision.model.OperatingSystem;
import studio.deervision.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueProperties issueProperties;

    @Autowired
    public IssueService(IssueRepository issueRepository, IssueProperties issueProperties) {
        this.issueRepository = issueRepository;
        this.issueProperties = issueProperties;
    }

    public void newIssue(String value, String userKey, String appId, String appVersion, OperatingSystem operatingSystem) {
        if(value == null || "".equals(value)) {
            throw new IllegalArgumentException("Empty issue value received");
        }
        String versionPattern = "^" + issueProperties.getVersionPattern() + "(-snapshot)?$";
        if(!Pattern.matches(versionPattern, appVersion)) {
            throw new IllegalArgumentException("Invalid application version: " + appVersion);
        }

        Issue issue = new Issue(value, userKey, appId, appVersion, operatingSystem);
        issueRepository.saveAndFlush(issue);
    }

    public Page<Issue> findPaginated(Pageable pageable) {
        return issueRepository.findAll(pageable);
    }

    public Issue findById(Long id) {
        Optional<Issue> issue = issueRepository.findById(id);
        return issue.orElseThrow(() -> new IllegalArgumentException("Error to find issue with ID : " + id));
    }

    public void removeById(Long id) {
        issueRepository.deleteById(id);
    }

    public Map<LocalDate, Long> findIssuesGroupByDate(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Issue> issues = issueRepository.findByDateTimeBetween(startDateTime, endDateTime);
        return issues.stream().collect(Collectors.groupingBy(bva -> bva.getDateTime().toLocalDate(), Collectors.counting()));
    }

}
