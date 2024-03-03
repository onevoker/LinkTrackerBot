package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class InvalidLinkResponseException extends ScrapperException {
    private static final String MESSAGE = "Invalid link";

    public InvalidLinkResponseException() {
        super(MESSAGE);
    }

    public InvalidLinkResponseException(String message) {
        super(message);
    }

    @Override
    public String getDescription() {
        return MESSAGE;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
