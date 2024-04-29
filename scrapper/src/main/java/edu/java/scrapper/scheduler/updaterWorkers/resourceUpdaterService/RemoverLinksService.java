package edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService;

import edu.java.dto.response.LinkUpdateResponse;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemoverLinksService {
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;
    private static final String LINK_WAS_REMOVED_BY_AUTHOR_DESCRIPTION =
        "Вы перестали ослеживать данную ссылку, так как она была удалена автором";

    @Transactional
    public LinkUpdateResponse removeLinkInDatabaseAndGetResponse(Long linkId, URI url) {
        List<Long> tgChatIdsForUpdate = chatLinkRepository.findTgChatIds(linkId);

        for (Long chatId : tgChatIdsForUpdate) {
            chatLinkRepository.remove(new ChatLink(chatId, linkId));
        }
        linkRepository.remove(linkId);

        return new LinkUpdateResponse(url, LINK_WAS_REMOVED_BY_AUTHOR_DESCRIPTION, tgChatIdsForUpdate);
    }
}
