package com.urchin.release.mgt.controller;

import com.google.common.base.CaseFormat;
import com.urchin.release.mgt.model.OperatingSystem;
import com.urchin.release.mgt.service.BinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/binaries")
public class BinaryRestController {

    private final BinaryService binaryService;

    @Autowired
    public BinaryRestController(BinaryService binaryService) {
        this.binaryService = binaryService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-UserKey: 0-17" --data "1.0.0" http://localhost:5000/api/binaries/linux/version
    @PostMapping(value="/{os}/version", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void postReleaseUsed(@PathVariable(name = "os") String os, @RequestBody String binaryVersion) {
        String userKey = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OperatingSystem operatingSystem = retrieveOperatingSystem(os);
        binaryService.newAuditVersion(binaryVersion, userKey, operatingSystem);
    }

    private OperatingSystem retrieveOperatingSystem(String os){
        String osString = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, os);
        return OperatingSystem.valueOf(osString);
    }
}
