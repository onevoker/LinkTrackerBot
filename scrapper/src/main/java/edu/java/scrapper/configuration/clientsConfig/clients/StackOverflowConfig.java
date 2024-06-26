package edu.java.scrapper.configuration.clientsConfig.clients;

import edu.java.scrapper.configuration.clientsConfig.ClientsConfig;
import edu.java.scrapper.retry.RetryFactory;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class StackOverflowConfig {
    private final ClientsConfig.StackOverflow stackOverflow;
    private final RetryFactory retryFactory;

    @Bean
    public WebClient stackOverflowWebClient() {
        var stackOverflowApi = stackOverflow.urls().api();

        return WebClient.builder()
            .baseUrl(stackOverflowApi)
            .build();
    }

    @Bean
    public Retry stackOverflowRetry() {
        var retrySettings = stackOverflow.retrySettings();
        return retryFactory.createRetry(retrySettings);
    }
}
