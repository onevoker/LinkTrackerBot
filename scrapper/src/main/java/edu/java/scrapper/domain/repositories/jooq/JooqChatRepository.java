package edu.java.scrapper.domain.repositories.jooq;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.domain.models.Chat;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.repositories.jooq.generated.Tables.CHAT;
import static edu.java.scrapper.domain.repositories.jooq.generated.Tables.CHAT_LINK;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dsl;

    @Override
    public void add(Long id) {
        try {
            dsl.insertInto(CHAT, CHAT.ID)
                .values(id)
                .execute();
        } catch (DataAccessException exception) {
            throw new ChatAlreadyRegisteredException();
        }
    }

    @Transactional
    @Override
    public int remove(Long id) {
        dsl.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(id))
            .execute();

        return dsl.deleteFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .execute();
    }

    @Override
    public List<Chat> findAll() {
        return dsl.selectFrom(CHAT)
            .fetchInto(Chat.class);
    }
}
