package edu.java.scrapper.senderUpdates.http;

import dto.response.ApiErrorResponse;
import dto.response.LinkUpdateResponse;
import edu.java.scrapper.clients.exceptions.ApiException;

import edu.java.scrapper.senderUpdates.UpdateSender;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class BotClient implements UpdateSender {
    private final WebClient botWebClient;
    private final Retry retry;
    private static final String UPDATE_ENDPOINT = "/updates";

    @Override
    public void sendUpdate(LinkUpdateResponse update) {
        botWebClient.post()
            .uri(UPDATE_ENDPOINT)
            .body(BodyInserters.fromValue(update))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .bodyToMono(Void.class)
            .transformDeferred(RetryOperator.of(retry))
            .block();
    }
}
