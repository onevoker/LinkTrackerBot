package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperTelegramChatClient;
import edu.java.bot.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnregisterCommandService implements CommandService {
    private final ScrapperTelegramChatClient chatClient;
    private static final String COMMAND = "/unregister";
    private static final String DESCRIPTION = "Удалить профиль";
    private static final String HANDLE_TEXT = "Чат успешно удален";

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        try {
            chatClient.unregisterChat(chatId);
            return new SendMessage(chatId, HANDLE_TEXT);
        } catch (ApiException exception) {
            return new SendMessage(chatId, exception.getMessage());
        }
    }
}
