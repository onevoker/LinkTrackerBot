package edu.java.scrapper.domain.services.interfaces;

import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import java.net.URI;

public interface LinkService {
    LinkResponse add(long tgChatId, URI url);

    LinkResponse remove(long tgChatId, URI url);

    ListLinksResponse listAll(long tgChatId);
}
