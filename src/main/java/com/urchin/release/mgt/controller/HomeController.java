package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.config.properties.BinaryProperties;
import com.urchin.release.mgt.config.properties.IssueProperties;
import com.urchin.release.mgt.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private IssueService issueService;
    private IssueProperties issueProperties;
    private BinaryProperties binaryProperties;

    @Autowired
    public HomeController(IssueService issueService, IssueProperties issueProperties, BinaryProperties binaryProperties){
        this.issueService = issueService;
        this.issueProperties = issueProperties;
        this.binaryProperties = binaryProperties;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "home");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        populateIssueChart(model);
        populateDownloadChart(model);
        populateAppVersionCheckChart(model);

        return "home.html";
    }

    private void populateIssueChart(Model model){
        List<LocalDate> chartDates = retrieveChartsDates(issueProperties.getChartDays());

        model.addAttribute("issueChartDates", chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .collect(Collectors.toList()));

        model.addAttribute("issueChartValues", chartDates.stream()
                .map(ld -> issueService.findByLocalDate(ld).size())
                .collect(Collectors.toList()));
    }

    private void populateDownloadChart(Model model){
        List<LocalDate> chartDates = retrieveChartsDates(binaryProperties.getChartDays());

        model.addAttribute("downloadChartDates", chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .collect(Collectors.toList()));
        //TODO ...
    }

    private void populateAppVersionCheckChart(Model model){
        List<LocalDate> chartDates = retrieveChartsDates(binaryProperties.getChartDays());

        model.addAttribute("appVersionCheckChartDates", chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .collect(Collectors.toList()));
        //TODO ...
    }

    private List<LocalDate> retrieveChartsDates(int nbChartDays) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = LocalDate.now().minusDays(nbChartDays);

        for(int i=0; i<nbChartDays; ++i) {
            currentDate = currentDate.plusDays(1);
            dates.add(currentDate);
        }

        return dates;
    }

}
