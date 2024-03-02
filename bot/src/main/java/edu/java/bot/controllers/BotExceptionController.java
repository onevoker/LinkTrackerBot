package edu.java.bot.controllers;

import edu.java.bot.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BotExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> stackTrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        String description = Arrays.toString(exception.getDetailMessageArguments());
        HttpStatusCode statusCode = exception.getStatusCode();
        String exceptionName = exception.getClass().getSimpleName();
        String exceptionMessage = exception.getMessage();

        return ApiErrorResponse.builder()
            .description(description)
            .code(statusCode.toString())
            .exceptionName(exceptionName)
            .exceptionMessage(exceptionMessage)
            .stacktrace(stackTrace)
            .build();
    }
}
