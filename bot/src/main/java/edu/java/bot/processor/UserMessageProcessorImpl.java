package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.exceptions.BlockedChatException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserMessageProcessorImpl implements UserMessageProcessor {
    private final Command helpCommand;
    private final Command listCommand;
    private final Command startCommand;
    private final Command trackCommand;
    private final Command untrackCommand;
    private static final String UNKNOWN_COMMAND_TEXT =
        "Мне не известна эта команда, для получения доступных команд воспользуйтесь командой /help";

    @Override
    public List<? extends Command> commands() {
        return List.of(
            helpCommand,
            listCommand,
            startCommand,
            trackCommand,
            untrackCommand
        );
    }

    @Override
    public SendMessage process(Update update) {
        Message message = update.message();
        try {
            String response = message.text();
            String inputCommand = response.split(" ")[0];

            for (var command : this.commands()) {
                if (inputCommand.equals(command.command())) {
                    return command.handle(update).parseMode(ParseMode.HTML);
                }
            }

            long chatId = message.chat().id();
            return new SendMessage(chatId, UNKNOWN_COMMAND_TEXT).parseMode(ParseMode.Markdown);
        } catch (NullPointerException exception) {
            throw new BlockedChatException();
        }
    }
}
