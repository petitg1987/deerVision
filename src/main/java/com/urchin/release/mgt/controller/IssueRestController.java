package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService){
        this.issueService = issueService;
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addIssue(@RequestBody String value, @RequestParam(value="appVersion") String appVersion){
        issueService.newIssue(value, appVersion);
    }
}
