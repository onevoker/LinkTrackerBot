package edu.java.scrapper.dto.stackOverflowDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Item(
    StackOverflowOwner owner,
    @JsonProperty("is_answered")
    boolean isAnswered,
    @JsonProperty("question_id")
    long questionId,
    @JsonProperty("answer_count")
    long answerCount,
    @JsonProperty("last_edit_date")
    OffsetDateTime lastEditDate,
    String link) {
}
