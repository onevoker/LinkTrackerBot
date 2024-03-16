package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import java.util.List;

public interface ChatLinkRepository {
    void add(ChatLink chatLink);

    int remove(ChatLink chatLink);

    List<ChatLink> findAll();

    List<Link> findLinksByTgChatId(Long tgChatId);

    List<Long> findTgChatIds(Long linkId);
}
