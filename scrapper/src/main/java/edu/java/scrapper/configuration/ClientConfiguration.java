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

    @Bean
    public WebClient gitHubWebClient() {
        HttpClient httpClient =
            HttpClient.create().responseTimeout(Duration.ofSeconds(applicationConfig.gitHubResponseTimeout()));
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
                    .header(HttpHeaders.AUTHORIZATION, applicationConfig.gitHubHeaderName() + token)
                    .build();
                return Mono.just(authorizedRequest);
            });
    }
}
