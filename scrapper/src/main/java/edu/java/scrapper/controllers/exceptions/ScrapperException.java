package edu.java.scrapper.controllers.exceptions;

import org.springframework.http.HttpStatusCode;

public abstract class ScrapperException extends RuntimeException {
    public ScrapperException(String message) {
        super(message);
    }

    public abstract String getDescription();

    public abstract HttpStatusCode getStatusCode();
}
