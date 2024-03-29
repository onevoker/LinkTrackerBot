package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandService {
    String command();

    String description();

    SendMessage handle(Update update);

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
