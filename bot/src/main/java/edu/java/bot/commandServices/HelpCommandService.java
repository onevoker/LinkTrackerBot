package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class HelpCommandService implements CommandService {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "Вам помогут";
    private static final String HANDLE_TEXT =
        """
            На данный момент поддерживается отслеживание ссылок с таких ресурсов как github и stackoverflow

            Доступные команды:
            /start -- зарегистрировать пользователя
            /help -- сообщение с подробным описанием команд
            /track ,,ссылка,, -- начать отслеживание ссылки
            /untrack ,,ссылка,, -- прекратить отслеживание ссылки
            /list -- показать список отслеживаемых ссылок
            /unregister -- удалить все данные(отслеживаемые ссылки), удалить профиль
            """;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public Mono<SendMessage> handle(Update update) {
        long chatId = update.message().chat().id();
        return Mono.just(new SendMessage(chatId, HANDLE_TEXT));
    }
}
