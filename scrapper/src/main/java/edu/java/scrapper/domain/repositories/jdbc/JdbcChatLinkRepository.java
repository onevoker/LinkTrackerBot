package edu.java.scrapper.domain.repositories.jdbc;

import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;

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

    @Override
    public int remove(ChatLink chatLink) {
        return jdbcTemplate.update(
            "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?",
            chatLink.getChatId(),
            chatLink.getLinkId()
        );
    }

    @Override
    public List<ChatLink> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat_link", new BeanPropertyRowMapper<>(ChatLink.class));
    }

    @Override
    public List<Link> findLinksByTgChatId(Long tgChatId) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE id IN (SELECT link_id FROM chat_link WHERE chat_id = ?)",
            new BeanPropertyRowMapper<>(Link.class),
            tgChatId
        );
    }

    @Override
    public List<Long> findTgChatIds(Long linkId) {
        return jdbcTemplate.query(
            "SELECT chat_id FROM chat_link WHERE link_id = ?",
            (rs, rowNum) -> rs.getLong("chat_id"),
            linkId
        );
    }

}
