package edu.java.gitHub.client;

import edu.java.gitHub.dto.RepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GitHubClient {
    private final WebClient gitHubWebClient;

    public Mono<RepositoryResponse> fetchRepository(String owner, String repository) {
        return gitHubWebClient.get()
            .uri(owner + "/" + repository)
            .retrieve()
            .bodyToMono(RepositoryResponse.class);
    }
}
