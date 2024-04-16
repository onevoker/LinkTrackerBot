package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import reactor.core.publisher.Mono;

public interface ResourceUpdaterService {
    Mono<LinkUpdateResponse> getLinkUpdateResponse(Link link);

    String getSupportedLinksDomain();
}
