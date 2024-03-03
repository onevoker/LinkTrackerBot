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
public class StartCommandService implements CommandService {
    private final ScrapperTelegramChatClient chatClient;
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "Начать работу с ботом";
    private static final String HANDLE_TEXT = "Начинаем регистрацию...\nДля получения списка команд используйте /help";

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
            chatClient.registerChat(chatId);
            return new SendMessage(chatId, HANDLE_TEXT);
        } catch (ApiException exception) {
            return new SendMessage(chatId, exception.getMessage());
        }
    }
}
