package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private final IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService){
        this.issueService = issueService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-UserKey: 0-17" --data "Error description" http://localhost:5000/api/issues?appVersion=1.0.0-SNAPSHOT
    //curl -X POST -H "Content-Type: text/plain" -H "X-UserKey: 0-17" --data-binary @test.txt http://localhost:5000/api/issues?appVersion=1.0.0-SNAPSHOT
    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addIssue(@RequestBody String value, @RequestParam(value="appVersion") String appVersion){
        String userKey = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        issueService.newIssue(value, userKey, appVersion);
    }
}
