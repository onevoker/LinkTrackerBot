package edu.java.scrapper.configuration;


import edu.java.scrapper.retry.retryes.FunctionalRetry;
import io.github.resilience4j.retry.Retry;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
@RequiredArgsConstructor
public class RetryConfig {
    private final ApplicationConfig.RetrySettings retrySettings;
    private final List<FunctionalRetry> retries;

    @Bean
    public Retry retry() {
        int retryCount = retrySettings.retryCount();
        Set<HttpStatus> httpStatuses = retrySettings.httpStatuses();
        Duration step = retrySettings.step();

        return retries.stream()
            .filter(e -> e.getBackOfType().equals(retrySettings.backOfType()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported back off type: "))
            .createRetry(retryCount, step, httpStatuses);
    }
}
