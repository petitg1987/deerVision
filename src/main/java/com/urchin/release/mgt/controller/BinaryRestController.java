package com.urchin.release.mgt.controller;

import com.google.common.base.CaseFormat;
import com.urchin.release.mgt.exception.BinaryNotFoundException;
import com.urchin.release.mgt.model.Binary;
import com.urchin.release.mgt.model.BinaryType;
import com.urchin.release.mgt.service.BinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

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
        Binary lastBinary = binaryService.getLastBinary(binaryType).orElseThrow(() -> new BinaryNotFoundException("No binary found for binary id: " + binaryId));
        String appVersion = lastBinary.getVersion();

        binaryService.newAuditVersion(appVersion, binaryType);
        return appVersion;
    }

    //curl -L http://localhost:8080/api/binaries/linux-deb --output release.deb
    @GetMapping(value="/{binaryId}")
    public ModelAndView download(@PathVariable(name = "binaryId") String binaryId){
        BinaryType binaryType = retrieveBinaryType(binaryId);
        Binary lastBinary = binaryService.getLastBinary(binaryType).orElseThrow(() -> new BinaryNotFoundException("No binary found for binary id: " + binaryId));

        binaryService.newAuditDownload(lastBinary.getVersion(), binaryType);
        return new ModelAndView("redirect:" + lastBinary.getUrl());
    }

    //curl -u "api:PASSWORD" -F 'file=@/home/greg/_binary/green-city-1.0.0.deb' -X PUT http://localhost:8080/api/binaries/linux-deb/
    @PutMapping(value="/{binaryId}")
    public void upload(@PathVariable(name = "binaryId") String binaryId, @RequestParam("file") MultipartFile file) {
        BinaryType binaryType = retrieveBinaryType(binaryId);
        if(file.getOriginalFilename()==null || !file.getOriginalFilename().endsWith(binaryType.getExtension())) {
            throw new IllegalArgumentException("Filename doesn't match with binary type: " + file.getOriginalFilename());
        }

        try {
            binaryService.uploadOrReplace(file.getOriginalFilename(), file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error to retrieve bytes of file: " + file.getOriginalFilename(), e);
        }
    }

    private BinaryType retrieveBinaryType(String binaryId){
        String binaryTypeString = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, binaryId);
        return BinaryType.valueOf(binaryTypeString);
    }
}
