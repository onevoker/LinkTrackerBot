package edu.java.scrapper.retry;

import edu.java.scrapper.configuration.resourcesConfig.ResourcesConfig;
import edu.java.scrapper.retry.retryes.FunctionalRetry;
import io.github.resilience4j.retry.Retry;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetryFactory {
    private final List<FunctionalRetry> retries;

    public Retry getRetry(ResourcesConfig.RetrySettings retrySettings) {
        BackOffType type = retrySettings.backOffType();
        int retryCount = retrySettings.retryCount();
        Set<HttpStatus> httpStatuses = retrySettings.httpStatuses();
        Duration step = retrySettings.step();

        return retries.stream()
            .filter(e -> e.getBackOfType().equals(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported back off type: "))
            .createRetry(retryCount, step, httpStatuses);
    }
}
