package edu.java.bot.repositories;

import com.pengrad.telegrambot.model.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<Long, User> users;

    public UserRepository() {
        this.users = new HashMap<>();
    }

    public boolean isRegistered(User user) {
        Long userId = user.id();
        if (users.containsKey(userId)) {
            return true;
        }
        users.put(userId, user);
        return false;
    }
}
