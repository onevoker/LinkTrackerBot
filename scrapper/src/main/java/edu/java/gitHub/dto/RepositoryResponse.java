package edu.java.gitHub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    long id,
    String name,
    @JsonProperty("updated_at") OffsetDateTime updatedAt,
    GitHubOwner owner) {
}
