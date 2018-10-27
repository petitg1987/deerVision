package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.service.BinaryService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/binaries")
public class BinaryController {

    private BinaryService binaryService;

    @Autowired
    public BinaryController(BinaryService binaryService){
        this.binaryService = binaryService;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "binaries");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String listBooks(Model model) {
        model.addAttribute("downloadsByVersionCount", binaryService.findDownloadsByVersionCount().stream()
                .sorted((d1, d2) -> d2.getAppVersion().compareTo(d1.getAppVersion()))
                .collect(Collectors.toList()));

        List<Path> binaryPath =  binaryService.getBinaryPaths();
        model.addAttribute("binaryFilesInfo", binaryPath.stream()
                .map(BinaryFileInfo::new)
                .collect(Collectors.toList()));

        return "binaries.html";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFile(@Param(value="filename") String filename, HttpServletResponse response) {
        InputStream issueValueStream = binaryService.getBinaryStream(filename);

        try {
            IOUtils.copy(issueValueStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException("Copy binary file in HTTP response fail for filename ID: " + filename);
        }
    }

    private static class BinaryFileInfo{

        private Path binaryFile;

        BinaryFileInfo(Path binaryFile){
            this.binaryFile = binaryFile;
        }

        public String getFileName(){
            return binaryFile.getFileName().toString();
        }

        public String getFileSizeMB(){
            double sizeDouble = binaryFile.toFile().length() / 1000.0 / 1000.0;
            return String.format("%.01f", sizeDouble);
        }
    }

}
