package edu.java.scrapper.domain.repositories.jpa.repos;

import edu.java.scrapper.domain.repositories.jpa.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatEntityRepository extends JpaRepository<ChatEntity, Long> {
}
