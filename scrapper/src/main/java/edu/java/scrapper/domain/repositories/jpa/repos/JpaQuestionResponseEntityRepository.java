package edu.java.scrapper.domain.repositories.jpa.repos;

import edu.java.scrapper.domain.repositories.jpa.entities.ItemEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionResponseEntityRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByLinkId(Long linkId);
}
