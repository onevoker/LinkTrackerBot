package edu.java.bot.updateHandlers.http.controllers;

import edu.java.bot.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class BotExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String description = Arrays.toString(exception.getDetailMessageArguments());
        HttpStatusCode statusCode = exception.getStatusCode();

        return ResponseEntity.status(statusCode).body(getDefaultErrorResponse(exception, statusCode, description));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(HandlerMethodValidationException exception) {
        String description = Arrays.toString(exception.getDetailMessageArguments());
        HttpStatusCode statusCode = exception.getStatusCode();

        return ResponseEntity.status(statusCode).body(getDefaultErrorResponse(exception, statusCode, description));
    }

    private ApiErrorResponse getDefaultErrorResponse(
        Exception exception,
        HttpStatusCode statusCode,
        String description
    ) {
        List<String> stackTrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
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
