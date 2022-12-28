package studio.deervision.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.deervision.config.properties.AppProperties;
import studio.deervision.exception.ApplicationException;
import studio.deervision.exception.IssueException;
import studio.deervision.model.Issue;
import studio.deervision.model.IssueOrigin;
import studio.deervision.model.OperatingSystem;
import studio.deervision.repository.IssueRepository;
import studio.deervision.repository.LightIssue;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final AppProperties appProperties;

    @Autowired
    public IssueService(IssueRepository issueRepository, AppProperties appProperties) {
        this.issueRepository = issueRepository;
        this.appProperties = appProperties;
    }

    public void newIssue(String value, IssueOrigin origin, String requestKey, String appId, String appVersion, OperatingSystem operatingSystem) throws ApplicationException, IssueException {
        if (value == null || "".equals(value)) {
            throw new IssueException("Empty issue value received");
        } else if (!Pattern.matches(appProperties.getVersionPattern(), appVersion)) {
            throw new ApplicationException("Invalid application version: " + appVersion);
        }

        Issue issue = new Issue(value, origin, requestKey, appId, appVersion, operatingSystem);
        issueRepository.saveAndFlush(issue);
    }

    public List<LightIssue> findAllOrderByDates() {
        return issueRepository.findAllOrderByDates();
    }

    public Issue findById(Long id) {
        Optional<Issue> issue = issueRepository.findById(id);
        return issue.orElseThrow(() -> new IllegalArgumentException("Error to find issue with ID : " + id));
    }

    @Transactional
    public void removeById(Long id) {
        issueRepository.deleteById(id);
    }

    @Transactional
    public void removeByRequestKey(String requestKey) {
        issueRepository.deleteByRequestKey(requestKey);
    }

}
