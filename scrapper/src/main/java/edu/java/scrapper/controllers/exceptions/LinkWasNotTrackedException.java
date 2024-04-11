package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class LinkWasNotTrackedException extends ScrapperException {
    private static final String MESSAGE = "Вы не отслеживаете данную ссылку";

    public LinkWasNotTrackedException() {
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
