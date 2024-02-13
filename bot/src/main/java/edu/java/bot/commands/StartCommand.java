package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.UserLinks;

public class StartCommand implements Command {
    private final UserLinks links;
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "Начать работу с ботом";
    private static final String HANDLE_TEXT = "Начинаем регистрацию...\nДля получения списка команд используйте /help";

    public StartCommand(UserLinks links) {
        this.links = links;
    }

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
        if (links.isRegistered(user)) {
            return new SendMessage(chatId, "Вы уже были зарегестрированы раньше");
        }

        return new SendMessage(chatId, HANDLE_TEXT);
    }
}
