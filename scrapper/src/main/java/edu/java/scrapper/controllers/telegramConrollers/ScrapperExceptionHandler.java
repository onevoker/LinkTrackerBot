package edu.java.scrapper.controllers.telegramConrollers;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.controllers.exceptions.ChatNotFoundException;
import edu.java.scrapper.controllers.exceptions.LinkWasNotTrackedException;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.controllers.exceptions.ScrapperException;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ScrapperExceptionHandler {
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

    @ExceptionHandler({
        ChatAlreadyRegisteredException.class,
        ChatNotFoundException.class,
        LinkWasTrackedException.class,
        LinkWasNotTrackedException.class
    })
    public ResponseEntity<ApiErrorResponse> handleScrapperExceptions(ScrapperException exception) {
        return ResponseEntity.status(exception.getStatusCode())
            .body(getApiErrorResponseForCustomExceptions(exception));
    }

    private ApiErrorResponse getApiErrorResponseForCustomExceptions(ScrapperException exception) {
        List<String> stackTrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();

        return ApiErrorResponse.builder()
            .description(exception.getDescription())
            .code(exception.getStatusCode().toString())
            .exceptionName(exception.getClass().getSimpleName())
            .exceptionMessage(exception.getMessage())
            .stacktrace(stackTrace)
            .build();
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
