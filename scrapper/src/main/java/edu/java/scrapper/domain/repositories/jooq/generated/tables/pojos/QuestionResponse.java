/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.repositories.jooq.generated.tables.pojos;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class QuestionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long questionId;
    private Long linkId;
    private Boolean answered;
    private Long answerCount;
    private OffsetDateTime lastActivityDate;

    public QuestionResponse() {
    }

    public QuestionResponse(QuestionResponse value) {
        this.questionId = value.questionId;
        this.linkId = value.linkId;
        this.answered = value.answered;
        this.answerCount = value.answerCount;
        this.lastActivityDate = value.lastActivityDate;
    }

    @ConstructorProperties({"questionId", "linkId", "answered", "answerCount", "lastActivityDate"})
    public QuestionResponse(
        @NotNull Long questionId,
        @Nullable Long linkId,
        @Nullable Boolean answered,
        @Nullable Long answerCount,
        @NotNull OffsetDateTime lastActivityDate
    ) {
        this.questionId = questionId;
        this.linkId = linkId;
        this.answered = answered;
        this.answerCount = answerCount;
        this.lastActivityDate = lastActivityDate;
    }

    /**
     * Getter for <code>QUESTION_RESPONSE.QUESTION_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getQuestionId() {
        return this.questionId;
    }

    /**
     * Setter for <code>QUESTION_RESPONSE.QUESTION_ID</code>.
     */
    public void setQuestionId(@NotNull Long questionId) {
        this.questionId = questionId;
    }

    /**
     * Getter for <code>QUESTION_RESPONSE.LINK_ID</code>.
     */
    @Nullable
    public Long getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>QUESTION_RESPONSE.LINK_ID</code>.
     */
    public void setLinkId(@Nullable Long linkId) {
        this.linkId = linkId;
    }

    /**
     * Getter for <code>QUESTION_RESPONSE.ANSWERED</code>.
     */
    @Nullable
    public Boolean getAnswered() {
        return this.answered;
    }

    /**
     * Setter for <code>QUESTION_RESPONSE.ANSWERED</code>.
     */
    public void setAnswered(@Nullable Boolean answered) {
        this.answered = answered;
    }

    /**
     * Getter for <code>QUESTION_RESPONSE.ANSWER_COUNT</code>.
     */
    @Nullable
    public Long getAnswerCount() {
        return this.answerCount;
    }

    /**
     * Setter for <code>QUESTION_RESPONSE.ANSWER_COUNT</code>.
     */
    public void setAnswerCount(@Nullable Long answerCount) {
        this.answerCount = answerCount;
    }

    /**
     * Getter for <code>QUESTION_RESPONSE.LAST_ACTIVITY_DATE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastActivityDate() {
        return this.lastActivityDate;
    }

    /**
     * Setter for <code>QUESTION_RESPONSE.LAST_ACTIVITY_DATE</code>.
     */
    public void setLastActivityDate(@NotNull OffsetDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuestionResponse other = (QuestionResponse) obj;
        if (this.questionId == null) {
            if (other.questionId != null) {
                return false;
            }
        } else if (!this.questionId.equals(other.questionId)) {
            return false;
        }
        if (this.linkId == null) {
            if (other.linkId != null) {
                return false;
            }
        } else if (!this.linkId.equals(other.linkId)) {
            return false;
        }
        if (this.answered == null) {
            if (other.answered != null) {
                return false;
            }
        } else if (!this.answered.equals(other.answered)) {
            return false;
        }
        if (this.answerCount == null) {
            if (other.answerCount != null) {
                return false;
            }
        } else if (!this.answerCount.equals(other.answerCount)) {
            return false;
        }
        if (this.lastActivityDate == null) {
            if (other.lastActivityDate != null) {
                return false;
            }
        } else if (!this.lastActivityDate.equals(other.lastActivityDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.questionId == null) ? 0 : this.questionId.hashCode());
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        result = prime * result + ((this.answered == null) ? 0 : this.answered.hashCode());
        result = prime * result + ((this.answerCount == null) ? 0 : this.answerCount.hashCode());
        result = prime * result + ((this.lastActivityDate == null) ? 0 : this.lastActivityDate.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QuestionResponse (");

        sb.append(questionId);
        sb.append(", ").append(linkId);
        sb.append(", ").append(answered);
        sb.append(", ").append(answerCount);
        sb.append(", ").append(lastActivityDate);

        sb.append(")");
        return sb.toString();
    }
}
