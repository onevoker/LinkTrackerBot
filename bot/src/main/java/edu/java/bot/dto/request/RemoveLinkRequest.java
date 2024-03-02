package edu.java.bot.dto.request;

import jakarta.validation.constraints.Pattern;

public record RemoveLinkRequest(
    @Pattern(regexp = "https?://.*")
    String link) {
}
