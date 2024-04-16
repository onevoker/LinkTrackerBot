package edu.java.scrapper.senderUpdates.http;

import edu.java.scrapper.clients.exceptions.ApiException;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.senderUpdates.UpdateSender;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BotClient implements UpdateSender {
    private final WebClient botWebClient;
    private final Retry retry;
    private static final String UPDATE_ENDPOINT = "/updates";

    @Override
    public Mono<Void> sendUpdate(LinkUpdateResponse update) {
        return botWebClient.post()
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
            .transformDeferred(RetryOperator.of(retry));
    }
}
