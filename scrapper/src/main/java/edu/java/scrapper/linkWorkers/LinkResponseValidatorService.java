package edu.java.scrapper.linkWorkers;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkResponseValidatorService {
    private final List<String> supportedDomains;

    public boolean isCorrectUri(URI url) {
        return supportedDomains.stream()
            .anyMatch(domain -> domain.equals(url.getHost()));
    }
}
