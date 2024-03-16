package edu.java.scrapper.dto.stackOverflowDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @JsonProperty("is_answered")
    private Boolean answered;

    @JsonProperty("question_id")
    private long questionId;

    @JsonProperty("answer_count")
    private long answerCount;

    @JsonProperty("last_activity_date")
    private OffsetDateTime lastActivityDate;
}
