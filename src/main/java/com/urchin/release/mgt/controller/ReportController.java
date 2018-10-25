package com.urchin.engine.monitor.controller;

import com.urchin.engine.monitor.config.ReportProperties;
import com.urchin.engine.monitor.model.Report;
import com.urchin.engine.monitor.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private ReportService reportService;
    private ReportProperties reportProperties;

    @Autowired
    public ReportController(ReportService reportService, ReportProperties reportProperties){
        this.reportService = reportService;
        this.reportProperties = reportProperties;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "report");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listBooks(Model model, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        Page<Report> reportPage = reportService.findPaginated(PageRequest.of(page - 1, reportProperties.getPageSize()));

        model.addAttribute("reportPage", reportPage);

        int totalPages = reportPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "reports.html";
    }

    @RequestMapping(value="/download", method=RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadFile(@Param(value="filename") String filename) {
        Report report = reportService.findByFilename(filename);
        return new FileSystemResource(new File(report.getFilePath()));
    }

    @RequestMapping(value="/remove", method=RequestMethod.GET)
    @ResponseBody
    public RedirectView removeFile(@Param(value="filename") String filename) {
        reportService.removeByFilename(filename);
        return new RedirectView("/reports/list");
    }

}
