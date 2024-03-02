package edu.java.scrapper.dto.response;

import jakarta.validation.constraints.Pattern;

public record LinkResponse(
    int id,
    @Pattern(regexp = "https?://.*")
    String url
) {
}
