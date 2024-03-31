package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class RateLimitException extends ScrapperException {
    private static final String MESSAGE = "Слишком много запросов, хватит дудосить!";

    public RateLimitException() {
        super(MESSAGE);
    }

    public RateLimitException(String message) {
        super(message);
    }

    @Override
    public String getDescription() {
        return MESSAGE;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.TOO_MANY_REQUESTS;
    }
}
