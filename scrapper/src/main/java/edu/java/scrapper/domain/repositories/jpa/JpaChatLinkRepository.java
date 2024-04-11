package edu.java.scrapper.domain.repositories.jpa;

import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.modelsMapper.ModelsMapper;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.jpa.entities.ChatLinkEntity;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaChatLinkEntityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class JpaChatLinkRepository implements ChatLinkRepository {
    private final JpaChatLinkEntityRepository chatLinkEntityRepository;
    private final ModelsMapper mapper;

    @Override
    public void add(ChatLink chatLink) {
        try {
            ChatLinkEntity chatLinkEntity = mapper.getChatLinkEntity(chatLink);
            chatLinkEntityRepository.save(chatLinkEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new LinkWasTrackedException();
        }
    }

    @Override
    public int remove(ChatLink chatLink) {
        ChatLinkEntity chatLinkEntity =
            chatLinkEntityRepository.findByChatIdAndLinkId(chatLink.getChatId(), chatLink.getLinkId());
        if (chatLinkEntity != null) {
            chatLinkEntityRepository.delete(chatLinkEntity);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<ChatLink> findAll() {
        return chatLinkEntityRepository.findAll().stream()
            .map(mapper::getChatLink)
            .collect(Collectors.toList());
    }

    @Override
    public List<Link> findLinksByTgChatId(Long tgChatId) {
        return chatLinkEntityRepository.findByChatId(tgChatId).stream()
            .map(chatLinkEntity -> mapper.getLink(chatLinkEntity.getLink()))
            .collect(Collectors.toList());

    }

    @Override
    public List<Long> findTgChatIds(Long linkId) {
        return chatLinkEntityRepository.findChatIdsByLinkId(linkId);
    }
}
