package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ChatAlreadyRegisteredException extends ScrapperException {
    private static final String MESSAGE = "Chat already registered";

    public ChatAlreadyRegisteredException() {
        super(MESSAGE);
    }

    public ChatAlreadyRegisteredException(String message) {
        super(message);
    }

    @Override
    public String getDescription() {
        return MESSAGE;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
