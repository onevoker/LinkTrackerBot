package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class LinkWasTrackedException extends ScrapperException {
    private static final String MESSAGE = "Ссылка уже добавлена, для просмотра ссылок введите /list";

    public LinkWasTrackedException() {
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
