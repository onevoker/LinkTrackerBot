package edu.java.scrapper.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record LinkUpdateRequest(
    int id,
    @Pattern(regexp = "https?://.*")
    String url,
    @NotBlank
    String description,
    @NotNull
    List<Integer> tgChatIds
) {
}
