package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/reports")
public class ReportRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportRestController.class);

    private ReportService reportService;

    @Autowired
    public ReportRestController(ReportService reportService){
        this.reportService = reportService;
    }

    @PostMapping(value="/", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addReport(@RequestBody String value, HttpServletRequest request){
        if(StringUtils.isEmpty(value)){
            LOGGER.warn("Empty error report received");
            return;
        }

        reportService.writeReport(value, request.getRemoteAddr());
    }
}
