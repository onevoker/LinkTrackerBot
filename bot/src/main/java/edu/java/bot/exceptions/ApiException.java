package edu.java.bot.exceptions;

import dto.response.ApiErrorResponse;

public class ApiException extends RuntimeException {

    public ApiException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.exceptionMessage());
    }
}
