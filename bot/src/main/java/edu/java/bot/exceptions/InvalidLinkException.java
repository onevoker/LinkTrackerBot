package edu.java.bot.exceptions;

public class InvalidLinkException extends RuntimeException {
    private static final String MESSAGE = "Invalid link";

    public InvalidLinkException() {
        super(MESSAGE);
    }
}
