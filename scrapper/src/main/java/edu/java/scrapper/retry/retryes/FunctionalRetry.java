package edu.java.scrapper.retry.retryes;


import edu.java.scrapper.retry.BackOfType;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Set;
import org.springframework.http.HttpStatus;

public interface FunctionalRetry {
    default Retry createRetry(int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
        return Retry.of(getBackOfType().name(), getRetryConfig(retryCount, step, httpStatuses));
    }

    BackOfType getBackOfType();

    RetryConfig getRetryConfig(int retryCount, Duration step, Set<HttpStatus> httpStatuses);
}
