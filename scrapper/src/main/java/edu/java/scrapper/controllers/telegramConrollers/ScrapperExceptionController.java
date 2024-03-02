package edu.java.scrapper.controllers.telegramConrollers;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.controllers.exceptions.ChatNotFoundException;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.controllers.exceptions.ScrapperException;
import org.springframework.http.HttpStatusCode;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ScrapperExceptionController {
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

    @ExceptionHandler(ChatAlreadyRegisteredException.class)
    public ApiErrorResponse handleChatAlreadyRegistered(ChatAlreadyRegisteredException exception) {
        return getErrorResponse(exception);
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ApiErrorResponse handleChatNotFound(ChatNotFoundException exception) {
        return getErrorResponse(exception);
    }

    @ExceptionHandler(LinkWasTrackedException.class)
    public ApiErrorResponse handleLinkWasTracked(LinkWasTrackedException exception) {
        return getErrorResponse(exception);
    }

    private ApiErrorResponse getErrorResponse(ScrapperException exception) {
        List<String> stackTrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();

        return ApiErrorResponse.builder()
            .description(exception.getDescription())
            .code(exception.getStatusCode().toString())
            .exceptionName(exception.getClass().getSimpleName())
            .exceptionMessage(exception.getMessage())
            .stacktrace(stackTrace)
            .build();
    }
}
