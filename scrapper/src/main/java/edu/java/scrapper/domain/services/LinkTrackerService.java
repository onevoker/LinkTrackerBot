package edu.java.scrapper.domain.services;

import edu.java.scrapper.controllers.exceptions.LinkWasNotTrackedException;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.services.interfaces.LinkService;
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
public class LinkTrackerService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkResponseFactory linkFactory;
    private static final String NOT_TRACKED_MESSAGE = "Вы не отслеживаете данную ссылку";

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
        URI linkUrl = linkFactory.normalizeUrl(url.toString());
        List<Link> linkInRepo = linkRepository.findByUrl(linkUrl);

        if (linkInRepo.isEmpty()) {
            throw new LinkWasNotTrackedException(NOT_TRACKED_MESSAGE);
        }

        Long linkId = linkInRepo.getFirst().getId();
        ChatLink chatLink = new ChatLink(tgChatId, linkId);
        int size = chatLinkRepository.remove(chatLink);

        if (size == 0) {
            throw new LinkWasNotTrackedException(NOT_TRACKED_MESSAGE);
        }

        List<Long> tgChatIds = chatLinkRepository.findTgChatIds(linkId);
        if (tgChatIds.isEmpty()) {
            linkRepository.remove(linkId);
        }

        return new LinkResponse(tgChatId, linkUrl);
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
        OffsetDateTime timeNow = OffsetDateTime.now(ZoneOffset.UTC);
        Link link = new Link(url, timeNow, timeNow);
        linkRepository.add(link);
        Link addedLink = linkRepository.findByUrl(url).getFirst();
        ChatLink chatLink = new ChatLink(tgChatId, addedLink.getId());
        chatLinkRepository.add(chatLink);
    }
}
