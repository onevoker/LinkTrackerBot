package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import java.util.List;

public interface ResourceUpdaterService {
    List<LinkUpdateRequest> getUpdates(List<Link> links);
}
