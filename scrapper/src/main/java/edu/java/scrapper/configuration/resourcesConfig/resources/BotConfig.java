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
public class BotConfig {
    private final ResourcesConfig.Bot bot;
    private final RetryFactory retryFactory;

    @Bean
    public WebClient botWebClient() {
        String baseBotUrl = bot.baseUrl();

        return WebClient.builder()
            .baseUrl(baseBotUrl)
            .build();
    }

    @Bean
    public Retry botRetry() {
        var retrySettings = bot.retrySettings();
        return retryFactory.getRetry(retrySettings);
    }
}
