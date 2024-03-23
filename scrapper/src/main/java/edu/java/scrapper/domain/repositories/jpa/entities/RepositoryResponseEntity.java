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
@Table(name = "repository_response")
public class RepositoryResponseEntity {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "link_id", nullable = false)
    private LinkEntity link;

    @Column(name = "pushed_at", nullable = false)
    private OffsetDateTime pushedAt;
}
