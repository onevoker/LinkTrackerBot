package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    @Bean
    Scheduler scheduler,
    Clients clients,
    String authorizationGitHubToken,
    String gitHubDomain,
    String stackOverflowDomain,
    String gitHubHeaderName,
    int gitHubResponseTimeout,
    @Bean
    GitHubRegexp gitHubRegexp,
    @Bean
    StackOverflowRegexp stackOverflowRegexp,
    String databaseAccessType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Clients(String gitHub, String stackOverflow, String bot) {
    }

    public record GitHubRegexp(String regexpForGitHubOwner, String regexpForGitHubRepo) {
    }

    public record StackOverflowRegexp(String regexpForStackOverflowQuestionId){}
}
