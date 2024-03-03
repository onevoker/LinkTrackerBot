package edu.java.scrapper.repositories;

import edu.java.scrapper.controllers.exceptions.LinkWasNotTrackedException;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class LinkResponseRepository {
    private final Map<Long, Set<LinkResponse>> links;

    public LinkResponseRepository() {
        this.links = new HashMap<>();
    }

    public void addUserLink(LinkResponse link) {
        long userId = link.id();

        if (isInUserLinks(link)) {
            throw new LinkWasTrackedException("Ссылка уже добавлена, для просмотра ссылок введите /list");
        }
        Set<LinkResponse> set = links.computeIfAbsent(userId, k -> new HashSet<>());
        set.add(link);
    }

    public void deleteUserLink(LinkResponse link) {
        long userId = link.id();

        if (!isInUserLinks(link)) {
            throw new LinkWasNotTrackedException("Вы не отслеживаете данную ссылку");
        }
        Set<LinkResponse> set = links.get(userId);
        set.remove(link);
    }

    public ListLinksResponse getUserLinks(long chatId) {
        Set<LinkResponse> set = links.computeIfAbsent(chatId, k -> new HashSet<>());
        return new ListLinksResponse(set.stream().toList(), set.size());
    }

    private boolean isInUserLinks(LinkResponse link) {
        try {
            return links.get(link.id()).contains(link);
        } catch (Exception e) {
            return false;
        }
    }
}
