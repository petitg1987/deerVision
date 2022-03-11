package studio.deervision.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.deervision.config.properties.IssueProperties;
import studio.deervision.model.Issue;
import studio.deervision.model.OperatingSystem;
import studio.deervision.repository.IssueRepository;
import studio.deervision.repository.LightIssue;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueProperties issueProperties;

    @Autowired
    public IssueService(IssueRepository issueRepository, IssueProperties issueProperties) {
        this.issueRepository = issueRepository;
        this.issueProperties = issueProperties;
    }

    public void newIssue(String value, String systemKey, String appId, String appVersion, OperatingSystem operatingSystem) {
        if(value == null || "".equals(value)) {
            throw new IllegalArgumentException("Empty issue value received");
        }
        String versionPattern = "^" + issueProperties.getVersionPattern() + "(-snapshot)?$";
        if(!Pattern.matches(versionPattern, appVersion)) {
            throw new IllegalArgumentException("Invalid application version: " + appVersion);
        }

        Issue issue = new Issue(value, systemKey, appId, appVersion, operatingSystem);
        issueRepository.saveAndFlush(issue);
    }

    public List<LightIssue> findAllOrderByDates() {
        return issueRepository.findAllOrderByDates();
    }

    public Issue findById(Long id) {
        Optional<Issue> issue = issueRepository.findById(id);
        return issue.orElseThrow(() -> new IllegalArgumentException("Error to find issue with ID : " + id));
    }

    public void removeById(Long id) {
        issueRepository.deleteById(id);
    }

}
