package edu.java.scrapper.dto.stackOverflowDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowOwner(
    @JsonProperty("account_id")
    long accountId,
    @JsonProperty("display_name")
    String displayName) {
}
