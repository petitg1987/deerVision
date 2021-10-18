package studio.deervision.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import studio.deervision.config.properties.IssueProperties;
import studio.deervision.model.Issue;
import studio.deervision.service.IssueService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;
    private final IssueProperties issueProperties;

    @Autowired
    public IssueController(IssueService issueService, IssueProperties issueProperties){
        this.issueService = issueService;
        this.issueProperties = issueProperties;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "issues");
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewIssues(Model model, @Param(value="id") Long id) {
        Issue issue = issueService.findById(id);
        model.addAttribute("issue", issue);

        return "issues-view.html";
    }

    @RequestMapping(value="/download", method=RequestMethod.GET)
    public void downloadFile(@Param(value="id") Long id, HttpServletResponse response) {
        Issue issue = issueService.findById(id);
        InputStream issueValueStream = new ByteArrayInputStream(issue.getValue().getBytes());

        try {
            IOUtils.copy(issueValueStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException("Copy issue in HTTP response fail for issue ID: " + id);
        }
    }

    @RequestMapping(value="/remove", method=RequestMethod.GET)
    @ResponseBody
    public RedirectView removeFile(@Param(value="id") Long id, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        issueService.removeById(id);
        return new RedirectView("/issues/list?page=" + page);
    }

}
