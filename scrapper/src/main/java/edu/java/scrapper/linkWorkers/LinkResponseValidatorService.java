package edu.java.scrapper.linkWorkers;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LinkResponseValidatorService {
    private static final String REGEX_FOR_GIT_HUB_REPO = "https://github\\.com/[^/]+/[^/]+/?";
    private static final String REGEX_FOR_STACK_OVERFLOW_QUESTION =
        "https://stackoverflow\\.com/questions/\\d+/[^/]+/?";
    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int BAD_STATUS_CODE = 300;

    public boolean isCorrectUri(URI url) {
        boolean isValidResource = isValidResource(url);
        if (isValidResource) {
            try {
                WebClient webClient = WebClient.create();
                Mono<Integer> responseCode = webClient.get()
                    .uri(url)
                    .exchangeToMono(response -> Mono.just(response.statusCode().value()));
                int code = responseCode.block();
                return code >= SUCCESS_STATUS_CODE && code < BAD_STATUS_CODE;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
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
