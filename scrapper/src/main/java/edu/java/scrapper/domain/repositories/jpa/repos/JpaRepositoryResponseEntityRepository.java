package edu.java.scrapper.domain.repositories.jpa.repos;

import edu.java.scrapper.domain.repositories.jpa.entities.RepositoryResponseEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRepositoryResponseEntityRepository extends JpaRepository<RepositoryResponseEntity, Long> {
    List<RepositoryResponseEntity> findByLinkId(Long linkId);
}
