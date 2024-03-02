package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ChatNotFoundException extends ScrapperException {
    private static final String MESSAGE = "Chat not found";

    public ChatNotFoundException() {
        super(MESSAGE);
    }

    public ChatNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getDescription() {
        return MESSAGE;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
