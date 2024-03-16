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
            addNoOneTrackedLink(tgChatId, linkUrl);
        } else {
            ChatLink chatLink = new ChatLink(tgChatId, linkInLinkRepos.getFirst().getId());
            chatLinkRepository.add(chatLink);
        }
        return linkResponse;
    }

    @Override
    public LinkResponse remove(long tgChatId, URI url) {
        Link addedLink = linkRepository.findByUrl(url).getFirst();
        Long linkId = addedLink.getId();
        ChatLink chatLink = new ChatLink(tgChatId, linkId);
        int size = chatLinkRepository.remove(chatLink);

        if (size == 0) {
            throw new LinkWasNotTrackedException("Вы не отслеживаете данную ссылку");
        }

        List<Long> tgChatIds = chatLinkRepository.findTgChatIds(linkId);
        if (tgChatIds.isEmpty()) {
            linkRepository.remove(linkId);
        }

        return linkFactory.createLink(tgChatId, url);
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        List<Link> links = chatLinkRepository.findLinksByTgChatId(tgChatId);
        List<LinkResponse> linkResponses = new ArrayList<>();

        for (var link : links) {
            linkResponses.add(new LinkResponse(link.getId(), link.getUrl()));
        }

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    private void addNoOneTrackedLink(long tgChatId, URI url) {
        OffsetDateTime timeNow = OffsetDateTime.now().with(ZoneOffset.UTC);
        Link link = new Link(url, timeNow, timeNow);
        linkRepository.add(link);
        Link addedLink = linkRepository.findByUrl(url).getFirst();
        ChatLink chatLink = new ChatLink(tgChatId, addedLink.getId());
        chatLinkRepository.add(chatLink);
    }
}
