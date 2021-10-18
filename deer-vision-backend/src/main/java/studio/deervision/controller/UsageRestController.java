package studio.deervision.controller;

import com.google.common.base.CaseFormat;
import studio.deervision.model.OperatingSystem;
import studio.deervision.service.UsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usage")
public class UsageRestController {

    private final UsageService usageService;

    @Autowired
    public UsageRestController(UsageService usageService) {
        this.usageService = usageService;
    }

    //curl -X POST -H "X-UserKey: 0-17" "http://localhost:5000/api/usage?appId=photonEngineer&appVersion=1.0.0&os=linux"
    //curl -X POST -H "X-UserKey: 0-17" "http://localhost:5000/api/usage?appId=photonEngineer&appVersion=1.0.0&os=windows"
    @PostMapping(value="")
    public void postUsage(@RequestParam("appId") String appId, @RequestParam("appVersion") String appVersion, @RequestParam("os") String os) {
        String userKey = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usageService.registerNewUsage(userKey, appId, appVersion, retrieveOperatingSystem(os));
    }

    private OperatingSystem retrieveOperatingSystem(String os){
        String osString = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, os);
        return OperatingSystem.valueOf(osString);
    }
}
