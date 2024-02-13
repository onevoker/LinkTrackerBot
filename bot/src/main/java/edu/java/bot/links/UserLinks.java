package edu.java.bot.links;

import com.pengrad.telegrambot.model.User;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UserLinks {
    private final Map<Long, Set<URI>> links;

    public UserLinks() {
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

    public void addUserLink(User user, String link) {
        Long userId = user.id();
        Set<URI> set = links.computeIfAbsent(userId, k -> new HashSet<>());
        set.add(normalizeLink(link));
    }

    public void deleteUserLink(User user, String link) {
        Set<URI> set = links.get(user.id());
        if (!set.isEmpty()) {
            set.remove(normalizeLink(link));
        }
    }

    public Set<URI> getUserLinks(User user) {
        Long userId = user.id();
        return links.get(userId);
    }

    public boolean isInUserLinks(User user, String link) {
        try {
            return this.getUserLinks(user).contains(normalizeLink(link));
        } catch (Exception e) {
            return false;
        }
    }

    private URI normalizeLink(String link) {
        try {
            return new URI(link.replaceAll("/+$", ""));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
