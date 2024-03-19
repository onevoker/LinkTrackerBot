package edu.java.scrapper.linkWorkers;

import edu.java.scrapper.controllers.exceptions.InvalidLinkResponseException;
import java.net.URI;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LinkResponseValidatorService {
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static final String RESOURCE_ERROR_MESSAGE =
        "Ссылка с данного сервера временно не доступна, он упал) Попробуйте позже";
    private static final String NOT_FOUND_MESSAGE = "Вы указали неправильную ссылку, возможно вам поможет /help";
    private static final String REGEX_FOR_GIT_HUB_REPO = "https://github\\.com/[^/]+/[^/]+/?";
    private static final String REGEX_FOR_STACK_OVERFLOW_QUESTION =
        "https://stackoverflow\\.com/questions/\\d+/[^/]+/?";

    public boolean isCorrectUri(URI url) {
        if (isValidResource(url)) {
            pingLink(url);
            return true;
        }
        return false;
    }

    private void pingLink(URI url) {
        WebClient.create().get()
            .uri(url)
            .retrieve()
            .onStatus(
                HttpStatusCode::is5xxServerError,
                response -> Mono.error(new InvalidLinkResponseException(RESOURCE_ERROR_MESSAGE))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> Mono.error(new InvalidLinkResponseException(NOT_FOUND_MESSAGE))
            )
            .bodyToMono(Void.class)
            .timeout(TIMEOUT)
            .block();
    }

    private boolean isValidResource(URI url) {
        return isGithubRepo(url) || isStackOverflowQuestion(url);
    }

    private boolean isGithubRepo(URI url) {
        return urlChecker(url, REGEX_FOR_GIT_HUB_REPO);
    }

    private boolean isStackOverflowQuestion(URI url) {
        return urlChecker(url, REGEX_FOR_STACK_OVERFLOW_QUESTION);
    }

    private boolean urlChecker(URI url, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url.toString());
        return matcher.matches();
    }
}
