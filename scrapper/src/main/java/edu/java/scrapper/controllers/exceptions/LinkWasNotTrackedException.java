package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class LinkWasNotTrackedException extends ScrapperException {
    private static final String MESSAGE = "Can't untrack, link wasn't tracked";

    public LinkWasNotTrackedException() {
        super(MESSAGE);
    }

    public LinkWasNotTrackedException(String message) {
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
