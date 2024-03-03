package edu.java.scrapper.repositories;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
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
        boolean answer = chatIds.getOrDefault(chatId, false);
        if (answer) {
            chatIds.put(chatId, true);
            throw new ChatAlreadyRegisteredException("Вы уже были зарегестрированы раньше");
        }
        chatIds.put(chatId, true);
    }
}
