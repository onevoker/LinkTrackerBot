package edu.java.scrapper.domain.jdbc.jdbcServices;

import edu.java.scrapper.controllers.exceptions.ChatNotFoundException;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.services.TgChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Override
    public void register(long tgChatId) {
        chatRepository.add(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) {
        List<Long> linkIds = chatRepository.remove(tgChatId);
        if (linkIds.isEmpty()) {
            throw new ChatNotFoundException("Вы не были зарегестрированы");
        }
        for (long linkId : linkIds) {
            List<Long> tgChatIds = chatLinkRepository.findTgChatIds(linkId);
            if (tgChatIds.isEmpty()) {
                linkRepository.remove(linkId);
            }
        }
    }
}
