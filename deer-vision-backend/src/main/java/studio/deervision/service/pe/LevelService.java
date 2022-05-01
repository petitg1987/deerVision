package studio.deervision.service.pe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.deervision.model.pe.LevelCompletionTime;
import studio.deervision.repository.LevelCompletionTimeRepository;

@Service
public class LevelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelService.class);

    private final LevelCompletionTimeRepository levelCompletionTimeRepository;

    @Autowired
    public LevelService(LevelCompletionTimeRepository levelCompletionTimeRepository) {
        this.levelCompletionTimeRepository = levelCompletionTimeRepository;
    }

    public void registerLevelCompletionTime(String requestKey, String appId, String appVersion, int levelId, long completionTimeInSec) {
        if (!appId.equals("photonEngineer")) {
            throw new IllegalArgumentException("Invalid application id: " + appId);
        } else if (completionTimeInSec > 60 * 40) {
            LOGGER.info("Ignore completion of {} for request key: {}", completionTimeInSec, requestKey);
            return;
        }
        //TODO check appId !
        levelCompletionTimeRepository.saveAndFlush(new LevelCompletionTime(requestKey, appVersion, levelId, completionTimeInSec));
    }

}
