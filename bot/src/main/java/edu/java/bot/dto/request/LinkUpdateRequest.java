package edu.java.bot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record LinkUpdateRequest(
    @Positive
    int id,
    @Pattern(regexp = "https?://.*")
    String url,
    @NotBlank
    String description,
    @NotNull
    List<Integer> tgChatIds
) {
}
