package studio.deervision.service.pe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.deervision.config.properties.AppProperties;
import studio.deervision.exception.ApplicationException;
import studio.deervision.model.pe.LevelCompletionTime;
import studio.deervision.repository.LevelCompletionTimeRepository;

import java.util.regex.Pattern;

@Service
public class LevelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelService.class);
    private static final int MAX_COMPLETION_TIME = 60 * 40;

    private final LevelCompletionTimeRepository levelCompletionTimeRepository;
    private final AppProperties appProperties;

    @Autowired
    public LevelService(LevelCompletionTimeRepository levelCompletionTimeRepository, AppProperties appProperties) {
        this.levelCompletionTimeRepository = levelCompletionTimeRepository;
        this.appProperties = appProperties;
    }

    public void registerLevelCompletionTime(String requestKey, String appId, String appVersion, int levelId, long completionTimeInSec) throws ApplicationException {
        if (!appId.equals("photonEngineer")) {
            throw new ApplicationException("Invalid application id: " + appId);
        } else if (!Pattern.matches(appProperties.getVersionPattern(), appVersion)) {
            throw new ApplicationException("Invalid application version: " + appVersion);
        } else if (completionTimeInSec > MAX_COMPLETION_TIME) {
            LOGGER.info("Ignore completion of {} seconds in level {} for request key: {}", completionTimeInSec, levelId, requestKey);
            return;
        }
        levelCompletionTimeRepository.saveAndFlush(new LevelCompletionTime(requestKey, appVersion, levelId, completionTimeInSec));
    }

}
