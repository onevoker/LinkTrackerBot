package edu.java.scrapper.repositories;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.controllers.exceptions.ChatNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ChatIdRepository {
    private final Map<Long, Boolean> chatIds;

    public ChatIdRepository() {
        this.chatIds = new HashMap<>();
    }

    public void registerChat(long chatId) {
        boolean isRegistered = chatIds.getOrDefault(chatId, false);
        if (isRegistered) {
            throw new ChatAlreadyRegisteredException("Вы уже были зарегестрированы раньше");
        } else {
            chatIds.put(chatId, true);
        }
    }

    public void unregisterChat(long chatId) {
        boolean isRegistered = chatIds.getOrDefault(chatId, false);

        if (isRegistered) {
            chatIds.remove(chatId);
        } else {
            throw new ChatNotFoundException("Вы не были зарегестрированы");
        }

    }
}
