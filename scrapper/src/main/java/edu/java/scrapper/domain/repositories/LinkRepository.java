package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.domain.models.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {
    void add(Link link);

    void remove(Long id);

    List<Link> findAll();

    List<Link> findByUrl(URI url);

    List<Link> findOldCheckedLinks(OffsetDateTime timestamp);

    void updateLastUpdate(OffsetDateTime time, Long id);

    void updateLastApiCheck(OffsetDateTime time, Long id);
}
