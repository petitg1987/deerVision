package com.urchin.release.mgt.controller;

import com.google.common.base.CaseFormat;
import com.urchin.release.mgt.model.Binary;
import com.urchin.release.mgt.model.BinaryType;
import com.urchin.release.mgt.service.BinaryService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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
    public void download(@PathVariable(name = "binaryId") String binaryId, HttpServletResponse response){
        BinaryType binaryType = retrieveBinaryType(binaryId);
        Binary binary = binaryService.getBinary(binaryType);

        String binaryFilename = binary.getFileName();
        response.addHeader("Content-Disposition", "attachment; filename=\"" + binaryFilename +"\"");
        response.addHeader("Content-Transfer-Encoding", "binary");

        try {
            InputStream binaryStream = Files.newInputStream(binary.getPath());
            IOUtils.copy(binaryStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException("Copy binary in HTTP response fail for binary ID: " + binaryId);
        }

        binaryService.newAuditDownload(binary.getVersion(), binaryType);
    }

    private BinaryType retrieveBinaryType(String binaryId){
        String binaryTypeString = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, binaryId);
        return BinaryType.valueOf(binaryTypeString);
    }
}
