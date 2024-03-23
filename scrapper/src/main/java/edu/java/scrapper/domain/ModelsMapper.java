package edu.java.scrapper.domain;

import edu.java.scrapper.domain.models.Chat;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.jpa.entities.ChatEntity;
import edu.java.scrapper.domain.repositories.jpa.entities.ChatLinkEntity;
import edu.java.scrapper.domain.repositories.jpa.entities.ItemEntity;
import edu.java.scrapper.domain.repositories.jpa.entities.LinkEntity;
import edu.java.scrapper.domain.repositories.jpa.entities.RepositoryResponseEntity;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaChatEntityRepository;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaLinkEntityRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelsMapper {
    private final JpaChatEntityRepository chatEntityRepository;
    private final JpaLinkEntityRepository linkEntityRepository;

    public Chat getChat(ChatEntity chatEntity) {
        return new Chat(chatEntity.getId());
    }

    public ChatLink getChatLink(ChatLinkEntity chatLinkEntity) {
        Long id = chatLinkEntity.getId();
        Long chatId = chatLinkEntity.getChat().getId();
        Long linkId = chatLinkEntity.getLink().getId();

        return new ChatLink(id, chatId, linkId);
    }

    public Link getLink(LinkEntity linkEntity) {
        Long id = linkEntity.getId();
        URI url = linkEntity.getUrl();
        OffsetDateTime lastUpdate = linkEntity.getLastUpdate();
        OffsetDateTime lastApiCheck = linkEntity.getLastApiCheck();

        return new Link(id, url, lastUpdate, lastApiCheck);
    }

    public RepositoryResponse getRepositoryResponse(RepositoryResponseEntity entity) {
        return new RepositoryResponse(entity.getId(), entity.getPushedAt());
    }

    public RepositoryResponseEntity getRepositoryResponseEntity(RepositoryResponse response, Long linkId) {
        LinkEntity linkEntity = linkEntityRepository.findById(linkId).get();
        RepositoryResponseEntity entity = new RepositoryResponseEntity();
        entity.setId(response.getId());
        entity.setPushedAt(response.getPushedAt());
        entity.setLink(linkEntity);
        return entity;
    }

    public Item getItem(ItemEntity entity) {
        return new Item(
            entity.getAnswered(),
            entity.getQuestionId(),
            entity.getAnswerCount(),
            entity.getLastActivityDate()
        );
    }

    public ItemEntity getItemEntity(Item item, Long linkId) {
        LinkEntity link = linkEntityRepository.findById(linkId).get();
        ItemEntity entity = new ItemEntity();
        entity.setLink(link);
        entity.setQuestionId(item.getQuestionId());
        entity.setAnswered(item.getAnswered());
        entity.setAnswerCount(item.getAnswerCount());
        entity.setLastActivityDate(item.getLastActivityDate());

        return entity;
    }

    public ChatLinkEntity getChatLinkEntity(ChatLink chatLink) {
        ChatEntity chatEntity = chatEntityRepository.findById(chatLink.getChatId()).get();
        LinkEntity linkEntity = linkEntityRepository.findById(chatLink.getLinkId()).get();

        return new ChatLinkEntity(chatLink.getId(), chatEntity, linkEntity);
    }
}
