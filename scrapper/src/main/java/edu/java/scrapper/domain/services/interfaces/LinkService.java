package edu.java.scrapper.domain.services.interfaces;

import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import java.net.URI;

public interface LinkService {
    LinkResponse add(long tgChatId, URI url);

    LinkResponse remove(long tgChatId, URI url);

    ListLinksResponse listAll(long tgChatId);
}
