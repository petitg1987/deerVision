package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.service.BinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/binaries")
public class BinaryController {

    private final BinaryService binaryService;

    @Autowired
    public BinaryController(BinaryService binaryService){
        this.binaryService = binaryService;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "binaries");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String binariesInfo(Model model, HttpServletRequest request) {
        model.addAttribute("downloadsByVersionCount", binaryService.findDownloadsByVersionCount().stream()
                .sorted((d1, d2) -> d2.getAppVersion().compareTo(d1.getAppVersion()))
                .collect(Collectors.toList()));

        model.addAttribute("binaries", binaryService.getLastBinaries());

        HttpRequest httpRequest = new ServletServerHttpRequest(request);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
        String requestUrl = uriComponents.toUri().toString();
        String baseServerUrl = requestUrl.substring(0, requestUrl.length() - request.getRequestURI().length());
        model.addAttribute("baseServerUrl", baseServerUrl);

        return "binaries.html";
    }

}
