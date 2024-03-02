package edu.java.bot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    int id,
    @NotNull
    URI url,
    @NotBlank
    String description,
    @NotNull
    List<Integer> tgChatIds) {
}