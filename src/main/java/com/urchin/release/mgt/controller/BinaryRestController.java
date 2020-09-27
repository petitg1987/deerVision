package com.urchin.release.mgt.controller;

import com.google.common.base.CaseFormat;
import com.urchin.release.mgt.model.BinaryType;
import com.urchin.release.mgt.service.BinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/binaries")
public class BinaryRestController {

    private final BinaryService binaryService;

    @Autowired
    public BinaryRestController(BinaryService binaryService) {
        this.binaryService = binaryService;
    }

    //curl -X POST -H "Content-Type: text/plain" --data "1.0.0" http://localhost:5000/api/binaries/linux-tar/version
    @PostMapping(value="/{binaryId}/version", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void postReleaseUsed(@PathVariable(name = "binaryId") String binaryId, @RequestBody String appVersion) {
        BinaryType binaryType = retrieveBinaryType(binaryId);
        binaryService.newAuditVersion(appVersion, binaryType);
    }

    private BinaryType retrieveBinaryType(String binaryId){
        String binaryTypeString = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, binaryId);
        return BinaryType.valueOf(binaryTypeString);
    }
}
