package edu.java.scrapper.domain.services;

import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import java.net.URI;

public interface LinkService {
    LinkResponse add(long tgChatId, URI url);

    LinkResponse remove(long tgChatId, URI url);

    ListLinksResponse listAll(long tgChatId);
}
