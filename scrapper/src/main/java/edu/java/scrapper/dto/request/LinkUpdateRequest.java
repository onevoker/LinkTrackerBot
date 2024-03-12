package edu.java.scrapper.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    int id,
    @NotBlank
    URI url,
    @NotBlank
    String description,
    @NotNull
    List<Integer> tgChatIds) {
}
