package edu.java.scrapper.retry.retries;

import edu.java.scrapper.retry.BackOffType;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ExponentialRetry implements FunctionalRetry {
    @Override
    public RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(retryCount)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(step))
            .retryOnException(e -> e instanceof WebClientResponseException
                && httpStatuses.contains(HttpStatus.resolve(((WebClientResponseException) e).getStatusCode().value())))
            .build();
    }

    @Override
    public BackOffType getBackOfType() {
        return BackOffType.EXPONENTIAL;
    }
}
