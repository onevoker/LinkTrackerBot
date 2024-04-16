package edu.java.scrapper.scheduler;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.scheduler.updaterWorkers.LinkUpdaterService;
import edu.java.scrapper.senderUpdates.UpdateSender;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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

        linkUpdaterService.getUpdates(timeNow)
            .flatMap(Flux::fromIterable)
            .flatMap(update -> updateSender.sendUpdate(update).thenReturn(update))
            .doOnError(error -> log.error(error.getMessage()))
            .subscribe();

        log.info("Checked updates");
    }
}
