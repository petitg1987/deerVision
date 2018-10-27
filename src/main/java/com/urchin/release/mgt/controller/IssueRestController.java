package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueRestController.class);

    private IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService){
        this.issueService = issueService;
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addIssue(@RequestBody String value, @RequestParam(value="appVersion") String appVersion, HttpServletRequest request){ //TODO store application version
        if(StringUtils.isEmpty(value)){
            LOGGER.warn("Empty issue received");
            return;
        }

        issueService.newIssue(value, request.getRemoteAddr());
    }
}
