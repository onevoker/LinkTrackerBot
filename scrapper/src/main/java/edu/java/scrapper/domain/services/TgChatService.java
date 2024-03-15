package edu.java.scrapper.domain.services;

public interface TgChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);
}
