package edu.java.scrapper.configuration.resourcesConfig.resources;

import edu.java.scrapper.configuration.resourcesConfig.ResourcesConfig;
import edu.java.scrapper.retry.RetryFactory;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class GitHubConfig {
    private final ResourcesConfig.GitHub gitHub;
    private final RetryFactory retryFactory;

    @Bean
    public WebClient gitHubWebClient() {
        var apiUrl = gitHub.urls().api();

        return WebClient.builder()
            .baseUrl(apiUrl)
            .build();
    }

    @Bean
    public Retry gitHubRetry() {
        var retrySettings = gitHub.retrySettings();
        return retryFactory.getRetry(retrySettings);
    }
}
