package edu.java.bot.links;

import com.pengrad.telegrambot.model.User;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository {
    private final Map<Long, Set<URI>> links;

    public LinkRepository() {
        this.links = new HashMap<>();
    }

    public boolean isRegistered(User user) {
        Long userId = user.id();
        if (links.containsKey(userId)) {
            return true;
        }
        links.computeIfAbsent(userId, k -> new HashSet<>());
        return false;
    }

    public void addUserLink(Link link) {
        Long userId = link.userId();
        Set<URI> set = links.computeIfAbsent(userId, k -> new HashSet<>());
        set.add(link.getUriLink());
    }

    public void deleteUserLink(Link link) {
        Long userId = link.userId();
        Set<URI> set = links.get(userId);
        set.remove(link.getUriLink());
    }

    public Set<URI> getUserLinks(User user) {
        Long userId = user.id();
        return links.get(userId);
    }

    public boolean isInUserLinks(Link link) {
        try {
            return links.get(link.userId()).contains(link.getUriLink());
        } catch (Exception e) {
            return false;
        }
    }
}
