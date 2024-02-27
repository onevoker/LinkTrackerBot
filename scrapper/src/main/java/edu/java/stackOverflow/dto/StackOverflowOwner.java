package edu.java.stackOverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowOwner(
    @JsonProperty("account_id")
    long accountId,
    @JsonProperty("display_name")
    String displayName) {
}
