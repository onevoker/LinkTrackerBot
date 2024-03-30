package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.dto.response.LinkUpdateResponse;

public interface ResourceUpdaterService {
    LinkUpdateResponse getLinkUpdateResponse(Link link);

    String getSupportedLinksDomain();
}
