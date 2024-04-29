package edu.java.bot.clients;


import dto.request.AddLinkRequest;
import dto.request.RemoveLinkRequest;
import dto.response.ApiErrorResponse;
import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import edu.java.bot.exceptions.ApiException;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScrapperLinkClient {
    private final WebClient scrapperWebClient;
    private final Retry retry;
    private static final String LINK_ENDPOINT_PATH = "/links";
    private static final String ID_HEADER = "Tg-Chat-Id";

    public Mono<ListLinksResponse> getTrackedLinks(long chatId) {
        return scrapperWebClient.get()
            .uri(LINK_ENDPOINT_PATH)
            .header(ID_HEADER, String.valueOf(chatId))
            .retrieve()
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .bodyToMono(ListLinksResponse.class)
            .transformDeferred(RetryOperator.of(retry));
    }

    public Mono<LinkResponse> trackLink(long chatId, AddLinkRequest addLinkRequest) {
        return scrapperWebClient.post()
            .uri(LINK_ENDPOINT_PATH)
            .header(ID_HEADER, String.valueOf(chatId))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .onStatus(
                HttpStatus.CONFLICT::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .bodyToMono(LinkResponse.class)
            .transformDeferred(RetryOperator.of(retry));
    }

    public Mono<LinkResponse> untrackLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return scrapperWebClient.method(HttpMethod.DELETE)
            .uri(LINK_ENDPOINT_PATH)
            .header(ID_HEADER, String.valueOf(chatId))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .onStatus(
                HttpStatus.CONFLICT::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .bodyToMono(LinkResponse.class)
            .transformDeferred(RetryOperator.of(retry));
    }
}
