package edu.java.bot.dto.response;

import jakarta.validation.constraints.Pattern;

public record LinkResponse(
    int id,
    @Pattern(regexp = "https?://.*")
    String url
) {
}
