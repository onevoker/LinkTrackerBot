package edu.java.scrapper.domain.jdbc.jdbcRepositories;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.domain.models.Chat;
import edu.java.scrapper.domain.repositories.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(Long id) {
        try {
            jdbcTemplate.update("INSERT INTO chat VALUES (?)", id);
        } catch (DuplicateKeyException exception) {
            throw new ChatAlreadyRegisteredException("Вы уже были зарегестрированы раньше");
        }
    }

    @Transactional
    @Override
    public List<Long> remove(Long id) {
        List<Long> linkIds = jdbcTemplate.query(
            "SELECT link_id FROM chat_link WHERE chat_id = ?",
            (rs, rowNum) -> rs.getLong("link_id"),
            id
        );

        jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = ?", id);
        jdbcTemplate.update("DELETE FROM chat WHERE id = ?", id);

        return linkIds;
    }

    @Transactional
    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(Chat.class));
    }
}
