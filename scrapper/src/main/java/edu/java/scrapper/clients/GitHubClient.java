package edu.java.scrapper.clients;

import edu.java.scrapper.clients.exceptions.RemovedLinkException;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GitHubClient {
    private final WebClient gitHubWebClient;
    private final Retry retry;

    public Mono<RepositoryResponse> fetchRepository(String owner, String repository) {
        return gitHubWebClient.get()
            .uri(owner + "/" + repository)
            .retrieve()
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                clientResponse -> Mono.error(RemovedLinkException::new)
            )
            .bodyToMono(RepositoryResponse.class)
            .transformDeferred(RetryOperator.of(retry));
    }
}
