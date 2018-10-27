package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.config.properties.IssueProperties;
import com.urchin.release.mgt.model.Issue;
import com.urchin.release.mgt.service.IssueService;
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
@RequestMapping("/issues")
public class IssueController {

    private IssueService issueService;
    private IssueProperties issueProperties;

    @Autowired
    public IssueController(IssueService issueService, IssueProperties issueProperties){
        this.issueService = issueService;
        this.issueProperties = issueProperties;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "issues");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listBooks(Model model, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        Page<Issue> issuePage = issueService.findPaginated(PageRequest.of(page - 1, issueProperties.getPageSize()));

        model.addAttribute("issuePage", issuePage);

        int totalPages = issuePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "issues.html";
    }

    @RequestMapping(value="/download", method=RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadFile(@Param(value="filename") String filename) {
        Issue issue = issueService.findByFilename(filename);
        return new FileSystemResource(new File(issue.getFilePath()));
    }

    @RequestMapping(value="/remove", method=RequestMethod.GET)
    @ResponseBody
    public RedirectView removeFile(@Param(value="filename") String filename) {
        issueService.removeByFilename(filename);
        return new RedirectView("/issues/list");
    }

}
