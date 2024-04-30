package edu.java.scrapper.clients;

import edu.java.scrapper.clients.exceptions.RemovedLinkException;
import edu.java.scrapper.dto.stackOverflowDto.QuestionResponse;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class StackOverflowClient {
    private final WebClient stackOverflowWebClient;
    private final Retry stackOverflowRetry;

    public QuestionResponse fetchQuestion(long questionId) {
        return stackOverflowWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("{id}")
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("site", "stackoverflow")
                .build(questionId))
            .retrieve()
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                clientResponse -> Mono.error(RemovedLinkException::new)
            )
            .bodyToMono(QuestionResponse.class)
            .transformDeferred(RetryOperator.of(stackOverflowRetry))
            .block();
    }
}
