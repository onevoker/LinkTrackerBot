package edu.java.bot.dto.request;

import jakarta.validation.constraints.Pattern;

public record AddLinkRequest(
    @Pattern(regexp = "https?://.*")
    String link) {
}
