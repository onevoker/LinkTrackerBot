package edu.java.scrapper.domain.repositories.jpa.repos;

import edu.java.scrapper.domain.repositories.jpa.entities.ChatLinkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaChatLinkEntityRepository extends JpaRepository<ChatLinkEntity, Long> {
    void deleteByChatId(Long chatId);

    List<ChatLinkEntity> findByChatId(Long chatId);

    @Query("SELECT cl.chat.id FROM ChatLinkEntity cl WHERE cl.link.id = :linkId")
    List<Long> findChatIdsByLinkId(Long linkId);

    ChatLinkEntity findByChatIdAndLinkId(Long chatId, Long linkId);
}
