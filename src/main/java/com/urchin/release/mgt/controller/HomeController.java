package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.config.properties.ReportProperties;
import com.urchin.release.mgt.service.ReportService;
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

    private ReportService reportService;
    private ReportProperties reportProperties;

    @Autowired
    public HomeController(ReportService reportService, ReportProperties reportProperties){
        this.reportService = reportService;
        this.reportProperties = reportProperties;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "home");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        List<LocalDate> chartDates = retrieveChartsDates();
        model.addAttribute("reportChartDates", chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .collect(Collectors.toList()));
        model.addAttribute("reportChartValues", chartDates.stream()
                .map(ld -> reportService.findByLocalDate(ld).size())
                .collect(Collectors.toList()));

        return "home.html";
    }


    private List<LocalDate> retrieveChartsDates() {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = LocalDate.now().minusDays(reportProperties.getChartDays());

        for(int i=0; i<reportProperties.getChartDays(); ++i) {
            currentDate = currentDate.plusDays(1);
            dates.add(currentDate);
        }

        return dates;
    }

}
