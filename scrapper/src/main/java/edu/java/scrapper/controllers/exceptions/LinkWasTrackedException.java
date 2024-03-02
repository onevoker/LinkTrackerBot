package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class LinkWasTrackedException extends ScrapperException {
    private static final String MESSAGE = "Link already tracks";

    public LinkWasTrackedException() {
        super(MESSAGE);
    }

    public LinkWasTrackedException(String message) {
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
