package edu.java.scrapper.scheduler;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.scheduler.updaterWorkers.LinkUpdaterService;
import edu.java.scrapper.senderUpdates.UpdateSender;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final ApplicationConfig applicationConfig;
    private final UpdateSender updateSender;
    private final LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "#{scheduler.interval()}")
    public void update() {
        Duration checkingDuration = applicationConfig.scheduler().forceCheckDelay();
        OffsetDateTime timeNow = OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(checkingDuration.toSeconds());
        List<LinkUpdateResponse> requests = linkUpdaterService.getUpdates(timeNow);

        if (!requests.isEmpty()) {
            for (var request : requests) {
                updateSender.sendUpdate(request);
            }
        } else {
            log.info("No updates");
        }
    }
}
