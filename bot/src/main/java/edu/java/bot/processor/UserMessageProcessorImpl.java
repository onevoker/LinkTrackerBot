package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.links.LinksRepository;
import java.util.List;

public class UserMessageProcessorImpl implements UserMessageProcessor {
    private final LinksRepository links = new LinksRepository();
    private static final String UNKNOWN_COMMAND_TEXT =
        "Мне не известна эта команда, для получения доступных команд воспользуйтесь командой /help";

    @Override
    public List<? extends Command> commands() {
        return List.of(
            new HelpCommand(),
            new ListCommand(links),
            new StartCommand(links),
            new TrackCommand(links),
            new UntrackCommand(links)
        );
    }

    @Override
    public SendMessage process(Update update) {
        Message message = update.message();
        String response = message.text();

        for (var command : this.commands()) {
            if (response != null && response.startsWith(command.command())) {
                return command.handle(update).parseMode(ParseMode.HTML);
            }
        }

        long chatId = message.chat().id();
        return new SendMessage(chatId, UNKNOWN_COMMAND_TEXT).parseMode(ParseMode.Markdown);
    }
}
