package edu.java.bot.messageProcessor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commandServices.CommandService;
import java.util.List;
import reactor.core.publisher.Mono;

public interface UserMessageProcessor {
    List<? extends CommandService> commands();

    Mono<SendMessage> process(Update update);
}
