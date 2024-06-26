package edu.java.bot.retry.retryes;

import edu.java.bot.retry.BackOffType;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ConstantRetry implements FunctionalRetry {
    @Override
    public RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(retryCount)
            .waitDuration(step)
            .retryOnException(e -> e instanceof WebClientResponseException
                && httpStatuses.contains(HttpStatus.resolve(((WebClientResponseException) e).getStatusCode().value())))
            .build();
    }

    @Override
    public BackOffType getBackOfType() {
        return BackOffType.CONSTANT;
    }
}
