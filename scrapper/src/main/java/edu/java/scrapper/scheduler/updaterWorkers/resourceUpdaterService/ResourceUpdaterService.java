package edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService;

import dto.response.LinkUpdateResponse;
import edu.java.scrapper.domain.models.Link;

public interface ResourceUpdaterService {
    LinkUpdateResponse getLinkUpdateResponse(Link link);

    String getSupportedLinksDomain();
}
