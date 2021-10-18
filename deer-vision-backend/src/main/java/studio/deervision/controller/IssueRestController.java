package studio.deervision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.model.OperatingSystem;
import studio.deervision.service.IssueService;

@RestController
@RequestMapping("/api")
public class IssueRestController {

    private final IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService){
        this.issueService = issueService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-UserKey: 0-17" --data "Error description" "http://localhost:5000/api/issues?appId=photonEngineer&appVersion=1.0.0&os=linux"
    //curl -X POST -H "Content-Type: text/plain" -H "X-UserKey: 0-17" --data-binary @test.txt "http://localhost:5000/api/issues?appId=photonEngineer&appVersion=1.0.0-snapshot&os=linux"
    @PostMapping(value = "/issues", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addIssue(@RequestBody String value, @RequestParam(value="appId") String appId, @RequestParam(value="appVersion") String appVersion, @RequestParam("os") String os) {
        String userKey = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        issueService.newIssue(value, userKey, appId, appVersion, OperatingSystem.retrieveOperatingSystem(os));
    }

}
