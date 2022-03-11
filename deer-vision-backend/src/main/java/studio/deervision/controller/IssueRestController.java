package studio.deervision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.dto.IssueDto;
import studio.deervision.dto.IssueListDto;
import studio.deervision.model.OperatingSystem;
import studio.deervision.repository.LightIssue;
import studio.deervision.service.IssueService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class IssueRestController {

    private final IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService) {
        this.issueService = issueService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-SystemKey: 0-17" --data "Error description" "http://localhost:5000/api/issues?appId=photonEngineer&appVersion=1.0.0&os=linux"
    //curl -X POST -H "Content-Type: text/plain" -H "X-SystemKey: 0-17" --data-binary @test.txt "http://localhost:5000/api/issues?appId=photonEngineer&appVersion=1.0.0-snapshot&os=linux"
    @PostMapping(value = "/issues", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addIssue(@RequestBody String value, @RequestParam(value="appId") String appId, @RequestParam(value="appVersion") String appVersion, @RequestParam("os") String os) {
        String systemKey = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        issueService.newIssue(value, systemKey, appId, appVersion, OperatingSystem.toOperatingSystem(os));
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/issues | jq .
    @GetMapping(value = "/admin/issues")
    public List<IssueListDto> listIssues() {
        List<IssueListDto> issuesListDto = new ArrayList<>();
        List<LightIssue> lightIssues = issueService.findAllOrderByDates();
        for (LightIssue lightIssue : lightIssues) {
            issuesListDto.add(new IssueListDto(lightIssue));
        }
        return issuesListDto;
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/issues/8 | jq .
    @GetMapping(value = "/admin/issues/{id}")
    public IssueDto getIssue(@PathVariable("id") Long id) {
        return new IssueDto(issueService.findById(id));
    }

    //curl -X DELETE -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/issues/2
    @DeleteMapping(value = "/admin/issues/{id}")
    public void deleteIssue(@PathVariable("id") Long id) {
        issueService.removeById(id);
    }

}
