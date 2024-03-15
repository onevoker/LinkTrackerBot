package edu.java.scrapper.domain.jdbc.jdbcServices;

import edu.java.scrapper.controllers.exceptions.LinkWasNotTrackedException;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.services.LinkService;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.linkWorkers.LinkResponseFactory;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkResponseFactory linkFactory;

    @Override
    public LinkResponse add(long tgChatId, URI url) {
        LinkResponse linkResponse = linkFactory.createLink(tgChatId, url);
        URI linkUrl = linkResponse.url();
        List<Link> linkInLinkRepos = linkRepository.findByUrl(linkUrl);

        if (linkInLinkRepos.isEmpty()) {
            OffsetDateTime timeNow = OffsetDateTime.now().with(ZoneOffset.UTC);
            Link link = new Link(linkUrl, timeNow, timeNow);
            linkRepository.add(link);
            Link addedLink = linkRepository.findByUrl(linkUrl).getFirst();
            ChatLink chatLink = new ChatLink(tgChatId, addedLink.getId());
            chatLinkRepository.add(chatLink);
        } else {
            ChatLink chatLink = new ChatLink(tgChatId, linkInLinkRepos.getFirst().getId());
            chatLinkRepository.add(chatLink);
        }
        return linkResponse;
    }

    @Override
    public LinkResponse remove(long tgChatId, URI url) {
        LinkResponse linkResponse = linkFactory.createLink(tgChatId, url);
        Link addedLink = linkRepository.findByUrl(url).getFirst();
        Long linkId = addedLink.getId();
        ChatLink chatLink = new ChatLink(tgChatId, linkId);
        int size = chatLinkRepository.remove(chatLink);

        if (size == 0) {
            throw new LinkWasNotTrackedException("Вы не отслеживаете данную ссылку");
        }

        List<Long> tgChatIds = chatLinkRepository.getTgChatIds(linkId);
        if (tgChatIds.isEmpty()) {
            linkRepository.remove(linkId);
        }

        return linkResponse;
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<Link> links = chatLinkRepository.getLinksByTgChatId(tgChatId);
        List<LinkResponse> linkResponses = new ArrayList<>();

        for (var link : links) {
            linkResponses.add(new LinkResponse(link.getId(), link.getUrl()));
        }

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }
}
