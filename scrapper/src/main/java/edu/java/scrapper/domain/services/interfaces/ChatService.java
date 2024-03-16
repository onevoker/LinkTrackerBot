package edu.java.scrapper.domain.services.interfaces;

public interface ChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);
}
