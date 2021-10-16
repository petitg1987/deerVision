package studio.deervision.controller;

import com.google.common.base.CaseFormat;
import studio.deervision.config.properties.BinaryProperties;
import studio.deervision.config.properties.IssueProperties;
import studio.deervision.model.OperatingSystem;
import studio.deervision.service.BinaryService;
import studio.deervision.service.IssueService;
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
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private static final String CHARTS_DATE_FORMAT = "dd-MM-yyyy";

    private final IssueService issueService;
    private final IssueProperties issueProperties;
    private final BinaryService binaryService;
    private final BinaryProperties binaryProperties;

    @Autowired
    public HomeController(IssueService issueService, IssueProperties issueProperties, BinaryService binaryService, BinaryProperties binaryProperties){
        this.issueService = issueService;
        this.issueProperties = issueProperties;
        this.binaryService = binaryService;
        this.binaryProperties = binaryProperties;
    }

    @ModelAttribute
    public void pageIdAttribute(Model model) {
        model.addAttribute("pageId", "home");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        populateIssueChart(model);
        populateBinariesUsageChart(model);

        return "home.html";
    }

    private void populateIssueChart(Model model){
        List<LocalDate> chartDates = retrieveChartsDates(issueProperties.getChartDays());
        LocalDate startDate = chartDates.get(0);
        LocalDate endDate = chartDates.get(chartDates.size() - 1);

        model.addAttribute("issueChartDates", chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern(CHARTS_DATE_FORMAT)))
                .collect(Collectors.toList()));

        Map<LocalDate, Long> mapIssues = addMissingDates(issueService.findIssuesGroupByDate(startDate, endDate), chartDates);
        model.addAttribute("issueChartValues", mapIssues.keySet().stream()
                .sorted()
                .map(mapIssues::get)
                .collect(Collectors.toList()));
    }

    private void populateBinariesUsageChart(Model model){
        List<LocalDate> chartDates = retrieveChartsDates(binaryProperties.getChartDays());
        LocalDate startDate = chartDates.get(0);
        LocalDate endDate = chartDates.get(chartDates.size() - 1);

        model.addAttribute("binaryUsageChartDates", chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern(CHARTS_DATE_FORMAT)))
                .collect(Collectors.toList()));

        for(OperatingSystem operatingSystem : OperatingSystem.values())
        {
            String operatingSystemString = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, operatingSystem.name());
            Map<LocalDate, Long> mapBinaryUsage = addMissingDates(binaryService.findBinaryVersionAuditsGroupByDate(operatingSystem, startDate, endDate), chartDates);
            model.addAttribute("binaryUsage" + operatingSystemString + "ChartValues", mapBinaryUsage.keySet().stream()
                    .sorted()
                    .map(mapBinaryUsage::get)
                    .collect(Collectors.toList()));
        }
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

    private Map<LocalDate, Long> addMissingDates(Map<LocalDate, Long> map, List<LocalDate> dates){
        dates.forEach(d -> map.putIfAbsent(d,  0L));
        return map;
    }

}
