package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ChatAlreadyRegisteredException extends ScrapperException {
    private static final String MESSAGE = "Вы уже были зарегестрированы раньше";

    public ChatAlreadyRegisteredException() {
        super(MESSAGE);
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
