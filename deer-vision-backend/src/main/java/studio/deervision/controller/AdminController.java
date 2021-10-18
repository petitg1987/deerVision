package studio.deervision.controller;

import com.google.common.base.CaseFormat;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.config.properties.AdminProperties;
import studio.deervision.config.properties.BinaryProperties;
import studio.deervision.dto.Token;
import studio.deervision.dto.Usage;
import studio.deervision.model.OperatingSystem;
import studio.deervision.service.BinaryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final String CHARTS_DATE_FORMAT = "dd-MM-yyyy";

    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;
    private final BinaryService binaryService;
    private final BinaryProperties binaryProperties;

    @Autowired
    public AdminController(AdminProperties adminProperties, PasswordEncoder passwordEncoder, BinaryService binaryService, BinaryProperties binaryProperties) {
        this.adminProperties = adminProperties;
        this.passwordEncoder = passwordEncoder;
        this.binaryService = binaryService;
        this.binaryProperties = binaryProperties;
    }

    //curl -X POST -d "password=dev" http://localhost:5000/api/admin/login
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

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/usage
    @GetMapping(value = "/usage")
    public Usage getUsage() { //TODO review...
        Usage usage = new Usage();

        List<LocalDate> chartDates = retrieveChartsDates(binaryProperties.getChartDays());
        LocalDate startDate = chartDates.get(0);
        LocalDate endDate = chartDates.get(chartDates.size() - 1);

        usage.setDates(chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern(CHARTS_DATE_FORMAT)))
                .collect(Collectors.toList()));

        for (OperatingSystem operatingSystem : OperatingSystem.values()) {
            String operatingSystemString = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, operatingSystem.name());
            Map<LocalDate, Long> mapBinaryUsage = addMissingDates(binaryService.findBinaryVersionAuditsGroupByDate(operatingSystem, startDate, endDate), chartDates);
            String key = "binaryUsage" + operatingSystemString + "ChartValues";
            List<Long> values = mapBinaryUsage.keySet().stream()
                    .sorted()
                    .map(mapBinaryUsage::get)
                    .collect(Collectors.toList());
            usage.addOsUsages(key, values);
        }

        return usage;
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