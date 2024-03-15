package edu.java.scrapper.domain.jdbc.jdbcRepositories;

import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(ChatLink chatLink) {
        try {
            jdbcTemplate.update(
                "INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)",
                chatLink.getChatId(),
                chatLink.getLinkId()
            );
        } catch (DataAccessException exception) {
            throw new LinkWasTrackedException("Ссылка уже добавлена, для просмотра ссылок введите /list");
        }
    }

    @Transactional
    @Override
    public int remove(ChatLink chatLink) {
        return jdbcTemplate.update(
            "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?",
            chatLink.getChatId(),
            chatLink.getLinkId()
        );
    }

    @Transactional
    @Override
    public List<ChatLink> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat_link", new BeanPropertyRowMapper<>(ChatLink.class));
    }

    @Transactional
    @Override
    public List<Link> getLinksByTgChatId(Long tgChatId) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE id IN (SELECT link_id FROM chat_link WHERE chat_id = ?)",
            new BeanPropertyRowMapper<>(Link.class),
            tgChatId
        );
    }

    @Transactional
    @Override
    public List<Long> getTgChatIds(Long linkId) {
        return jdbcTemplate.query(
            "SELECT chat_id FROM chat_link WHERE link_id = ?",
            (rs, rowNum) -> rs.getLong("chat_id"),
            linkId
        );
    }

}
