package edu.java.stackOverflow.client;

import edu.java.stackOverflow.dto.QuestionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class StackOverflowClient {
    private final WebClient stackOverflowWebClient;

    public Mono<QuestionResponse> fetchQuestion(long questionId) {
        return stackOverflowWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("{id}")
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .build(questionId))
            .retrieve()
            .bodyToMono(QuestionResponse.class);
    }
}
