package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commandServices.Command;
import java.util.List;

public interface UserMessageProcessor {
    List<? extends Command> commands();

    SendMessage process(Update update);
}
