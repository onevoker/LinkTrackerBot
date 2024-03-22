package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import java.util.List;

public interface ResourceUpdaterService {
    List<LinkUpdateResponse> getListLinkUpdateResponses(List<Link> links);
}
