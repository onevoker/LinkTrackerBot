package edu.java.scrapper.clients;

import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GitHubClient {
    private final WebClient gitHubWebClient;

    public RepositoryResponse fetchRepository(String owner, String repository) {
        return gitHubWebClient.get()
            .uri(owner + "/" + repository)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block();
    }
}
