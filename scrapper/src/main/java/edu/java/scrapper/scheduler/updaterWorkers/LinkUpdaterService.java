package edu.java.scrapper.scheduler.updaterWorkers;

import edu.java.dto.response.LinkUpdateResponse;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService.ResourceUpdaterService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterService {
    private final LinkRepository linkRepository;
    private final List<ResourceUpdaterService> updaterServices;

    public List<LinkUpdateResponse> getUpdates(OffsetDateTime time) {
        List<Link> neededToCheckLinks = linkRepository.findOldCheckedLinks(time);
        if (neededToCheckLinks.isEmpty()) {
            return Collections.emptyList();
        }

        List<LinkUpdateResponse> allUpdates = new ArrayList<>();

        for (var link : neededToCheckLinks) {
            URI url = link.getUrl();
            for (var updaterService : updaterServices) {
                if (url.getHost().equals(updaterService.getSupportedLinksDomain())) {
                    LinkUpdateResponse updateResponse = updaterService.getLinkUpdateResponse(link);
                    if (updateResponse != null) {
                        allUpdates.add(updateResponse);
                    }
                }
            }
        }
        return allUpdates;

    }
}
