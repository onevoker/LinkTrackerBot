package edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public WebClient gitHubWebClient() {
        String baseGithubUrl = applicationConfig.urls().gitHubBaseUrl();

        return WebClient.builder()
            .baseUrl(baseGithubUrl)
            .build();
    }

    @Bean
    public WebClient stackOverflowWebClient() {
        String baseStackoverflowUrl = applicationConfig.urls().stackOverflowBaseUrl();

        return WebClient.builder()
            .baseUrl(baseStackoverflowUrl)
            .build();
    }

    @Bean
    public WebClient botWebClient() {
        String baseBotUrl = applicationConfig.botBaseUrl();

        return WebClient.builder()
            .baseUrl(baseBotUrl)
            .build();
    }
}
