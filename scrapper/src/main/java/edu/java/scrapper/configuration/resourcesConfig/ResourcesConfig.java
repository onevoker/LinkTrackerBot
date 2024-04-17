package edu.java.scrapper.configuration.resourcesConfig;

import edu.java.scrapper.retry.BackOffType;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "resources", ignoreUnknownFields = false)
public record ResourcesConfig(
    @Bean
    Bot bot,
    @Bean
    StackOverflow stackOverflow,
    @Bean
    GitHub gitHub
) {
    public record Bot(
        String baseUrl,
        RetrySettings retrySettings
    ) {
    }

    public record StackOverflow(
        ResourceUrls urls,
        StackOverflowRegexps regexps,
        RetrySettings retrySettings
    ) {
    }

    public record GitHub(
        ResourceUrls urls,
        int responseTimeout,
        GitHubAuthorization authorization,
        GitHubRegexps regexps,
        RetrySettings retrySettings
    ) {
    }

    public record StackOverflowRegexps(String regexpForQuestionId) {
    }

    public record GitHubRegexps(String regexpForGitHubOwner, String regexpForGitHubRepo) {
    }

    public record GitHubAuthorization(String headerName, String authorizationToken) {
    }

    public record ResourceUrls(String api, String domain) {
    }

    public record RetrySettings(BackOffType backOffType, int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
    }
}
