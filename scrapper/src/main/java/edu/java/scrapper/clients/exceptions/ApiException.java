package edu.java.scrapper.clients.exceptions;

import edu.java.scrapper.dto.response.ApiErrorResponse;

public class ApiException extends RuntimeException {

    public ApiException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.exceptionMessage());
    }
}
