package edu.java.bot.linkTrackerBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.exceptions.BlockedChatException;
import edu.java.bot.messageProcessor.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class LinkTrackerBot implements Bot {
    private final UserMessageProcessor userMessageProcessor;

    private final ApplicationConfig applicationConfig;
    private TelegramBot bot;
    private static final String EXECUTED_MESSAGE_FROM_BLOCKED_CHAT = "Разблокировали заблокированный чат";

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        Flux.fromIterable(updates)
            .flatMap(update -> userMessageProcessor.process(update)
                .flatMap(sendMessage ->
                    {
                        SendResponse sendResponse = bot.execute(sendMessage);
                        if (!sendResponse.isOk()) {
                            log.error("{} - {}", sendResponse.errorCode(), sendResponse.description());
                        }
                        return Mono.empty();
                    }
                )
            )
            .doOnError(BlockedChatException.class, exception ->
                log.info(EXECUTED_MESSAGE_FROM_BLOCKED_CHAT))
            .subscribe();
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        bot = new TelegramBot(applicationConfig.telegramToken());
        bot.setUpdatesListener(this);
        createMenu();
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
    }

    private void createMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        userMessageProcessor.commands().forEach(command -> listOfCommands.add(command.toApiCommand()));
        BotCommand[] commandsArray = listOfCommands.toArray(new BotCommand[0]);
        this.execute(new SetMyCommands(commandsArray));
    }
}
