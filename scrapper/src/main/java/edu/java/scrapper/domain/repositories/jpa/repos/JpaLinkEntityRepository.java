package edu.java.scrapper.domain.repositories.jpa.repos;

import edu.java.scrapper.domain.repositories.jpa.entities.LinkEntity;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLinkEntityRepository extends JpaRepository<LinkEntity, Long> {
    List<LinkEntity> findByUrl(URI url);

    List<LinkEntity> findLinkEntitiesByLastApiCheckLessThan(OffsetDateTime time);
}

