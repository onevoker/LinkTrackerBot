package edu.java.scrapper.configuration;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig applicationConfig;
    private static final String HEADER_NAME = "onevoker";
    private static final int RESPONSE_TIMEOUT = 15;

    @Bean
    public WebClient gitHubWebClient() {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(RESPONSE_TIMEOUT));
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        String baseGithubUrl = applicationConfig.clients().gitHub();

        return WebClient.builder()
            .baseUrl(baseGithubUrl)
            .clientConnector(connector)
            .filter(addAuthorization(applicationConfig.authorizationGitHubToken()))
            .build();
    }

    @Bean
    public WebClient stackOverflowWebClient() {
        String baseStackoverflowUrl = applicationConfig.clients().stackOverflow();

        return WebClient.builder()
            .baseUrl(baseStackoverflowUrl)
            .build();
    }

    @Bean
    public WebClient botWebClient() {
        String baseBotUrl = applicationConfig.clients().bot();

        return WebClient.builder()
            .baseUrl(baseBotUrl)
            .build();
    }

    private ExchangeFilterFunction addAuthorization(String token) {
        return ExchangeFilterFunction.ofRequestProcessor(
            clientRequest -> {
                ClientRequest authorizedRequest = ClientRequest.from(clientRequest)
                    .header(HttpHeaders.AUTHORIZATION, HEADER_NAME + token)
                    .build();
                return Mono.just(authorizedRequest);
            });
    }
}
