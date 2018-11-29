package com.urchin.release.mgt.controller;

import com.google.common.base.CaseFormat;
import com.urchin.release.mgt.model.Binary;
import com.urchin.release.mgt.model.BinaryType;
import com.urchin.release.mgt.service.BinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/binaries")
public class BinaryRestController {

    private BinaryService binaryService;

    @Autowired
    public BinaryRestController(BinaryService binaryService) {
        this.binaryService = binaryService;
    }

    //curl http://localhost:8080/api/binaries/linux-tar/version
    @GetMapping(value="/{binaryId}/version", produces = MediaType.TEXT_PLAIN_VALUE)
    public String lastVersion(@PathVariable(name = "binaryId") String binaryId){
        BinaryType binaryType = retrieveBinaryType(binaryId);
        String appVersion = binaryService.getBinary(binaryType).getVersion();

        binaryService.newAuditVersion(appVersion, binaryType);
        return appVersion;
    }

    //wget -O release.tar.bz2 http://localhost:8080/api/binaries/linux-tar
    @GetMapping(value="/{binaryId}")
    public ModelAndView download(@PathVariable(name = "binaryId") String binaryId){
        BinaryType binaryType = retrieveBinaryType(binaryId);
        Binary binary = binaryService.getBinary(binaryType);

        binaryService.newAuditDownload(binary.getVersion(), binaryType);
        return new ModelAndView("redirect:" + binary.getUrl());
    }

    private BinaryType retrieveBinaryType(String binaryId){
        String binaryTypeString = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, binaryId);
        return BinaryType.valueOf(binaryTypeString);
    }
}
