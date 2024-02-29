package edu.java.bot.exceptions;

public class BlockedChatException extends NullPointerException {
    private static final String MESSAGE = "Chat is null, because bot was blocked";

    public BlockedChatException() {
        super(MESSAGE);
    }
}
