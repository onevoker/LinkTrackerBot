package edu.java.stackOverflow.client;

import edu.java.stackOverflow.dto.QuestionResponse;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<QuestionResponse> fetchQuestion(long questionId);
}
