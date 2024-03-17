package edu.java.scrapper.domain.repositories.jooq;

import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.repositories.jooq.generated.Tables.LINK;

@Repository
@RequiredArgsConstructor
@Log4j2
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dsl;

    @Transactional
    @Override
    public void add(Link link) {
        String url = link.getUrl().toString();
        OffsetDateTime lastUpdate = link.getLastUpdate();
        OffsetDateTime lastApiCheck = link.getLastUpdate();
        try {
            dsl.insertInto(LINK, LINK.URL, LINK.LAST_UPDATE, LINK.LAST_API_CHECK)
                .values(url, lastUpdate, lastApiCheck)
                .execute();
        } catch (DataAccessException ignored) {
            log.info("Добавили опять какую-то популярную ссылку)");
        }
    }

    @Transactional
    @Override
    public void remove(Long id) {
        dsl.deleteFrom(LINK)
            .where(LINK.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public List<Link> findAll() {
        return dsl.selectFrom(LINK)
            .fetchInto(Link.class);
    }

    @Transactional
    @Override
    public List<Link> findByUrl(URI url) {
        return dsl.selectFrom(LINK)
            .where(LINK.URL.eq(url.toString()))
            .fetchInto(Link.class);
    }

    @Transactional
    @Override
    public List<Link> findOldCheckedLinks(OffsetDateTime time) {
        return dsl.selectFrom(LINK)
            .where(LINK.LAST_API_CHECK.lt(time))
            .fetchInto(Link.class);
    }

    @Transactional
    @Override
    public void updateLastUpdate(OffsetDateTime time, Long id) {
        dsl.update(LINK)
            .set(LINK.LAST_UPDATE, time)
            .where(LINK.ID.eq(id))
            .execute();
    }

    @Transactional
    @Override
    public void updateLastApiCheck(OffsetDateTime time, Long id) {
        dsl.update(LINK)
            .set(LINK.LAST_API_CHECK, time)
            .where(LINK.ID.eq(id))
            .execute();
    }
}
