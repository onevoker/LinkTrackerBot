package edu.java.bot.repositories;

import com.pengrad.telegrambot.model.User;
import edu.java.bot.links.Link;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository {
    private final Map<Long, Set<Link>> links;

    public LinkRepository() {
        this.links = new HashMap<>();
    }

    public void addUserLink(Link link) {
        Long userId = link.userId();
        Set<Link> set = links.computeIfAbsent(userId, k -> new HashSet<>());
        set.add(link);
    }

    public void deleteUserLink(Link link) {
        Long userId = link.userId();
        Set<Link> set = links.get(userId);
        set.remove(link);
    }

    public Set<Link> getUserLinks(User user) {
        Long userId = user.id();
        return links.get(userId);
    }

    public boolean isInUserLinks(Link link) {
        try {
            return links.get(link.userId()).contains(link);
        } catch (Exception e) {
            return false;
        }
    }
}
