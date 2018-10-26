package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.model.BinaryType;
import com.urchin.release.mgt.service.BinaryService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/binary") //TODO REST: /api/binaries/windows/[version] | /api/binaries/linux-tar/[version] | /api/binaries/linux-deb/[version]
public class BinaryRestController {

    private BinaryService binaryService;

    @Autowired
    public BinaryRestController(BinaryService binaryService) {
        this.binaryService = binaryService;
    }

    //curl -u api:[PASSWORD] http://localhost:8080/api/binary/version/?type=LINUX_TAR
    @GetMapping(value="/version")
    public String getLastVersion(@RequestParam(name = "type") BinaryType binaryType){
        return binaryService.getBinaryVersion(binaryType);
    }

    //wget --auth-no-challenge --user api --password [PASSWORD] -O release.tar.bz2 "http://localhost:8080/api/binary?type=LINUX_TAR"
    @GetMapping
    public void getLastBinary(@RequestParam(name = "type") BinaryType binaryType, HttpServletResponse response){
        InputStream binaryStream = binaryService.getBinaryStream(binaryType);

        try {
            IOUtils.copy(binaryStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to copy binary stream in response for binary type: " + binaryType);
        }
    }
}
