package edu.java.bot.messageProcessor;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commandServices.CommandService;
import edu.java.bot.exceptions.BlockedChatException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserMessageProcessorService implements UserMessageProcessor {
    private final List<CommandService> commands;
    private static final String UNKNOWN_COMMAND_TEXT =
        "Мне не известна эта команда, для получения доступных команд воспользуйтесь командой /help";

    @Override
    public List<? extends CommandService> commands() {
        return commands;
    }

    @Override
    public Mono<SendMessage> process(Update update) {
        Message message = update.message();
        if (message == null || message.chat() == null) {
            throw new BlockedChatException();
        }

        String response = message.text();
        String inputCommand = response.split(" ")[0];
        return Flux.fromIterable(this.commands)
            .filter(commandService -> inputCommand.equals(commandService.command()))
            .flatMap(commandService -> commandService.handle(update))
            .next()
            .switchIfEmpty(Mono.just(new SendMessage(
                    message.chat().id(),
                    UNKNOWN_COMMAND_TEXT
                ).parseMode(ParseMode.Markdown))
            );
    }
}
