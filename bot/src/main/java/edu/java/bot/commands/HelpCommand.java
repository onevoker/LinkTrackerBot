package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "Вам помогут";
    private static final String HANDLE_TEXT =
        "На данный момент поддерживается отслеживание ссылок с таких ресурсов как github и stackoverflow\n\n"
            + "Доступные команды:\n"
            + "/start -- зарегистрировать пользователя\n"
            + "/help -- сообщение с подробным описанием команд\n"
            + "/track ,,ссылка,, -- начать отслеживание ссылки\n"
            + "/untrack ,,ссылка,, -- прекратить отслеживание ссылки\n"
            + "/list -- показать список отслеживаемых ссылок\n";

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
        long chatId = update.message().chat().id();
        return new SendMessage(chatId, HANDLE_TEXT);
    }
}
