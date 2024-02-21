package edu.java.stackOverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Item(
    StackOverflowOwner owner,
    @JsonProperty("question_id") long questionId,
    @JsonProperty("answer_count") int answerCount,
    @JsonProperty("last_edit_date") OffsetDateTime lastEditDate) {
}
