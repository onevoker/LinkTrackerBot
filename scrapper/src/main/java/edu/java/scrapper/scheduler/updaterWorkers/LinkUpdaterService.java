package edu.java.scrapper.scheduler.updaterWorkers;

import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService.ResourceUpdaterService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class LinkUpdaterService {
    private final LinkRepository linkRepository;
    private final List<ResourceUpdaterService> updaterServices;

    public Flux<List<LinkUpdateResponse>> getUpdates(OffsetDateTime time) {
        return Flux.fromIterable(linkRepository.findOldCheckedLinks(time))
            .flatMap(link -> {
                    URI url = link.getUrl();
                    return Flux.fromIterable(updaterServices)
                        .filter(updaterService -> url.getHost().equals(updaterService.getSupportedLinksDomain()))
                        .flatMap(updaterService -> updaterService.getLinkUpdateResponse(link))
                        .collect(Collectors.toList());
                }
            )
            .defaultIfEmpty(Collections.emptyList());
    }
}
