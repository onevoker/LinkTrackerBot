package edu.java.bot.clients;

import edu.java.bot.exceptions.ApiException;
import edu.java.dto.response.ApiErrorResponse;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScrapperTelegramChatClient {
    private static final String TELEGRAM_CHAT_ENDPOINT_PATH = "/tg-chat/";
    private static final String ID_HEADER = "Tg-Chat-Id";
    private final WebClient scrapperWebClient;
    private final Retry retry;

    public Mono<Void> registerChat(long id) {
        return scrapperWebClient.post()
            .uri(TELEGRAM_CHAT_ENDPOINT_PATH + id)
            .header(ID_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatus.CONFLICT::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .bodyToMono(Void.class)
            .transformDeferred(RetryOperator.of(retry));
    }

    public Mono<Void> unregisterChat(long id) {
        return scrapperWebClient.delete()
            .uri(TELEGRAM_CHAT_ENDPOINT_PATH + id)
            .header(ID_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
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

