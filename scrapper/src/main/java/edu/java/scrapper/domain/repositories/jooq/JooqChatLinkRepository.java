package edu.java.scrapper.domain.repositories.jooq;

import edu.java.scrapper.controllers.exceptions.ChatNotFoundException;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import static edu.java.scrapper.domain.repositories.jooq.generated.Tables.CHAT_LINK;
import static edu.java.scrapper.domain.repositories.jooq.generated.Tables.LINK;

@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {
    private final DSLContext dsl;

    @Override
    public void add(ChatLink chatLink) {
        try {
            dsl.insertInto(CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
                .values(chatLink.getChatId(), chatLink.getLinkId())
                .execute();
        } catch (DuplicateKeyException exception) {
            throw new LinkWasTrackedException();
        } catch (DataAccessException exception) {
            throw new ChatNotFoundException();
        }
    }

    @Override
    public int remove(ChatLink chatLink) {
        return dsl.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatLink.getChatId()))
            .and(CHAT_LINK.LINK_ID.eq(chatLink.getLinkId()))
            .execute();
    }

    @Override
    public List<ChatLink> findAll() {
        return dsl.selectFrom(CHAT_LINK)
            .fetchInto(ChatLink.class);
    }

    @Override
    public List<Link> findLinksByTgChatId(Long tgChatId) {
        return dsl.selectFrom(LINK)
            .where(LINK.ID.in(
                    dsl.select(CHAT_LINK.LINK_ID)
                        .from(CHAT_LINK)
                        .where(CHAT_LINK.CHAT_ID.eq(tgChatId))
                )
            )
            .fetchInto(Link.class);
    }

    @Override
    public List<Long> findTgChatIds(Long linkId) {
        return dsl.select(CHAT_LINK.CHAT_ID)
            .from(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(Long.class);
    }
}
