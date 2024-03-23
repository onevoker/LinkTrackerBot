package edu.java.scrapper.domain.repositories.jpa.entities;

import jakarta.persistence.Column;
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
    @Column(name = "question_id")
    private Long questionId;

    @OneToOne
    @JoinColumn(name = "link_id", nullable = false)
    private LinkEntity link;

    @Column(nullable = false)
    private Boolean answered;

    @Column(name = "answer_count", nullable = false)
    private Long answerCount;

    @Column(name = "last_activity_date", nullable = false)
    private OffsetDateTime lastActivityDate;
}
