package edu.java.scrapper.domain.repositories.jdbc;

import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

//@Repository
@RequiredArgsConstructor
@Log4j2
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Link link) {
        String url = link.getUrl().toString();
        OffsetDateTime lastUpdate = link.getLastUpdate();
        OffsetDateTime lastApiCheck = link.getLastUpdate();
        try {
            jdbcTemplate.update(
                "INSERT INTO link (url, last_update, last_api_check) VALUES (?, ?, ?)",
                url,
                lastUpdate,
                lastApiCheck
            );
        } catch (DuplicateKeyException ignored) {
            log.info("Добавили опять какую-то популярную ссылку)");
        }
    }

    @Override
    public void remove(Long id) {
        jdbcTemplate.update("DELETE FROM link WHERE id = ?", id);
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM link", new BeanPropertyRowMapper<>(Link.class));
    }

    @Override
    public List<Link> findByUrl(URI url) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE url = ?",
            new BeanPropertyRowMapper<>(Link.class),
            url.toString()
        );
    }

    @Override
    public List<Link> findOldCheckedLinks(OffsetDateTime time) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE last_api_check < ?",
            new BeanPropertyRowMapper<>(Link.class),
            time
        );
    }

    @Override
    public void updateLastUpdate(OffsetDateTime time, Long id) {
        jdbcTemplate.update("UPDATE link SET last_update = ? WHERE id = ?", time, id);
    }

    @Override
    public void updateLastApiCheck(OffsetDateTime time, Long id) {
        jdbcTemplate.update("UPDATE link SET last_api_check = ? WHERE id = ?", time, id);
    }
}
