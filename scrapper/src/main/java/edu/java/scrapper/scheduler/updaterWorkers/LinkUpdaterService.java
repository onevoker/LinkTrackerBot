package edu.java.scrapper.scheduler.updaterWorkers;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcLinkRepository;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService.ResourceUpdaterService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterService {
    private final ApplicationConfig applicationConfig;
    private final JdbcLinkRepository linkRepository;
    private final ResourceUpdaterService stackOverflowUpdaterService;
    private final ResourceUpdaterService gitHubUpdaterService;

    public List<LinkUpdateRequest> getUpdates(OffsetDateTime time) {
        List<Link> neededToCheckLinks = linkRepository.findOldCheckedLinks(time);
        if (neededToCheckLinks.isEmpty()) {
            return List.of();
        }

        List<Link> stackOverflowLinks = new ArrayList<>();
        List<Link> gitHubLinks = new ArrayList<>();

        for (var link : neededToCheckLinks) {
            URI url = link.getUrl();
            if (url.getHost().equals(applicationConfig.gitHubDomain())) {
                gitHubLinks.add(link);
            } else if (url.getHost().equals(applicationConfig.stackOverflowDomain())) {
                stackOverflowLinks.add(link);
            }
        }

        List<LinkUpdateRequest> gitHubUpdates = gitHubUpdaterService.getUpdates(gitHubLinks);
        List<LinkUpdateRequest> stackOverflowUpdates = stackOverflowUpdaterService.getUpdates(stackOverflowLinks);

        List<LinkUpdateRequest> allUpdates = new ArrayList<>(gitHubUpdates.size() + stackOverflowUpdates.size());
        allUpdates.addAll(gitHubUpdates);
        allUpdates.addAll(stackOverflowUpdates);

        return allUpdates;
    }
}
