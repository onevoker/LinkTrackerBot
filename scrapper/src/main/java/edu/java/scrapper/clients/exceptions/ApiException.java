package edu.java.scrapper.clients.exceptions;

import dto.response.ApiErrorResponse;

public class ApiException extends RuntimeException {

    public ApiException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.exceptionMessage());
    }
}
