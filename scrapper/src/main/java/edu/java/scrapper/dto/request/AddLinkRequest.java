package edu.java.scrapper.dto.request;

import jakarta.validation.constraints.Pattern;

public record AddLinkRequest(
    @Pattern(regexp = "https?://.*")
    String link) {
}
