package edu.java.scrapper.domain.repositories.jdbc;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.domain.models.Chat;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

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
    public int remove(Long id) {
        jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = ?", id);

        return jdbcTemplate.update("DELETE FROM chat WHERE id = ?", id);
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(Chat.class));
    }
}
