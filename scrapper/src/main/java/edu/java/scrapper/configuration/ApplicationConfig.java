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
    @Bean
    DataSourceValues dataSourceValues,
    String authorizationGitHubToken,
    String gitHubDomain,
    String stackOverflowDomain
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Clients(String gitHub, String stackOverflow, String bot) {
    }

    public record DataSourceValues(String driverClassName, String url, String username, String password) {
    }
}
