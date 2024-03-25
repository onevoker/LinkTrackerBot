package edu.java.bot.linkValidators;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.exceptions.InvalidLinkException;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LinkResponseValidatorService {
    private final ApplicationConfig applicationConfig;
    private static final String RESOURCE_ERROR_MESSAGE =
        "Ссылка с данного сервера временно недоступна, он упал) Попробуйте позже";
    private static final String NOT_FOUND_MESSAGE = "Вы указали неправильную ссылку, возможно вам поможет /help";

    public boolean isCorrectUri(URI url) {
        if (!isValidResource(url)) {
            return false;
        }
        pingLink(url);
        return true;
    }

    private void pingLink(URI url) {
        WebClient.create().get()
            .uri(url)
            .retrieve()
            .onStatus(
                HttpStatusCode::is5xxServerError,
                response -> Mono.error(new InvalidLinkException(RESOURCE_ERROR_MESSAGE))
            )
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> Mono.error(new InvalidLinkException(NOT_FOUND_MESSAGE))
            )
            .bodyToMono(Void.class)
            .timeout(applicationConfig.responseTimeout())
            .block();
    }

    private boolean isValidResource(URI url) {
        List<String> resourceCheckers = applicationConfig.validatorRegexp();
        return resourceCheckers.stream().anyMatch(regexp -> urlChecker(url, regexp));
    }

    private boolean urlChecker(URI url, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url.toString());
        return matcher.matches();
    }
}
