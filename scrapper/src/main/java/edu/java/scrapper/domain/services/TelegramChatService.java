package edu.java.scrapper.domain.services;

import edu.java.scrapper.controllers.exceptions.ChatNotFoundException;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.services.interfaces.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramChatService implements ChatService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Override
    public void register(long tgChatId) {
        chatRepository.add(tgChatId);
    }

    @Transactional
    @Override
    public void unregister(long tgChatId) {
        List<Link> links = chatLinkRepository.findLinksByTgChatId(tgChatId);
        int size = chatRepository.remove(tgChatId);
        if (size == 0) {
            throw new ChatNotFoundException("Вы не были зарегестрированы");
        }
        if (!links.isEmpty()) {
            deleteNoOneTrackedLinks(links);
        }
    }

    private void deleteNoOneTrackedLinks(List<Link> links) {
        for (Link link : links) {
            Long linkId = link.getId();
            List<Long> tgChatIds = chatLinkRepository.findTgChatIds(linkId);
            if (tgChatIds.isEmpty()) {
                linkRepository.remove(linkId);
            }
        }
    }
}
