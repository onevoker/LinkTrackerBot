package edu.java.scrapper.domain.repositories.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question_response")

public class ItemEntity {
    @Id
    private Long questionId;

    @OneToOne
    @JoinColumn(name = "link_id", nullable = false)
    private LinkEntity link;

    private Boolean answered;

    private Long answerCount;

    private OffsetDateTime lastActivityDate;
}
