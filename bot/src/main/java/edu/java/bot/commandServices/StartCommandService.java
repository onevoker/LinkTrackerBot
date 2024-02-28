package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartCommandService implements Command {
    private final UserRepository userRepository;
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "Начать работу с ботом";
    private static final String HANDLE_TEXT = "Начинаем регистрацию...\nДля получения списка команд используйте /help";
    private static final String REGISTERED_TEXT = "Вы уже были зарегестрированы раньше";

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
        User user = message.from();
        if (userRepository.isRegistered(user)) {
            return new SendMessage(chatId, REGISTERED_TEXT);
        }

        return new SendMessage(chatId, HANDLE_TEXT);
    }
}
