package studio.deervision.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.config.properties.AdminProperties;
import studio.deervision.config.properties.UsageProperties;
import studio.deervision.dto.Token;
import studio.deervision.dto.UsageInfo;
import studio.deervision.service.UsageService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private static final String CHARTS_DATE_FORMAT = "dd-MM-yyyy";

    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;
    private final UsageService usageService;
    private final UsageProperties usageProperties;

    @Autowired
    public AdminRestController(AdminProperties adminProperties, PasswordEncoder passwordEncoder, UsageService usageService, UsageProperties usageProperties) {
        this.adminProperties = adminProperties;
        this.passwordEncoder = passwordEncoder;
        this.usageService = usageService;
        this.usageProperties = usageProperties;
    }

    //curl -X POST http://localhost:5000/api/admin/login?password=dev
    @PostMapping("login")
    public Token login(@RequestParam("password") String password) {
        Token tokenDto = new Token();
        if (!passwordEncoder.matches(password, adminProperties.getPassword())) {
            tokenDto.setValue("");
        } else {
            tokenDto.setValue(getJWTToken());
        }
        return tokenDto;
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/usage | jq .
    @GetMapping(value = "/usage")
    public UsageInfo getUsage() {
        UsageInfo usageInfo = new UsageInfo();

        List<LocalDate> chartDates = retrieveChartsDates(usageProperties.getChartDays());
        LocalDate startDate = chartDates.get(0);
        LocalDate endDate = chartDates.get(chartDates.size() - 1);

        usageInfo.setDates(chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern(CHARTS_DATE_FORMAT)))
                .collect(Collectors.toList()));

        List<String> appIds = usageService.findDistinctAppId();
        for(String appId : appIds) {
            Map<LocalDate, Long> mapAppUsages = addMissingDates(usageService.findUsagesBetweenDates(appId, startDate, endDate), chartDates);
            List<Long> appUsages = mapAppUsages.keySet().stream()
                    .sorted()
                    .map(mapAppUsages::get)
                    .collect(Collectors.toList());
            usageInfo.addAppUsage(appId, appUsages);
        }

        return usageInfo;
    }

    private String getJWTToken() {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts
                .builder()
                .setId("dvsJWT")
                .setSubject("admin")
                .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 365L * 10L))
                .signWith(SignatureAlgorithm.HS512, adminProperties.getJwtSecret().getBytes()).compact();
        return "Bearer " + token;
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