package edu.java.gitHub.client;

import edu.java.gitHub.dto.RepositoryResponse;
import reactor.core.publisher.Mono;

public interface GitHubClient {
    Mono<RepositoryResponse> fetchRepository(String owner, String repository);
}
