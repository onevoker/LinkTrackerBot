package edu.java.scrapper.clients.exceptions;

public class RemovedLinkException extends RuntimeException {
    private static final String MESSAGE = "Ссылка была удалена";

    public RemovedLinkException() {
        super(MESSAGE);
    }
}
