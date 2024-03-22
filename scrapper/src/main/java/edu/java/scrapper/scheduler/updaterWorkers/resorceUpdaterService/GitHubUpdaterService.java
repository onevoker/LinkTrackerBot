package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.linkParser.dto.GitHubLinkData;
import edu.java.scrapper.linkParser.services.GitHubParserService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GitHubUpdaterService implements ResourceUpdaterService {
    private final GitHubParserService gitHubParserService;
    private final GitHubResponseRepository gitHubResponseRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final GitHubClient gitHubClient;
    private static final String UPDATE_DESCRIPTION = "Появилось обновление";

    @Transactional
    @Override
    public List<LinkUpdateResponse> getListLinkUpdateResponses(List<Link> links) {
        List<LinkUpdateResponse> requests = new ArrayList<>();

        for (var link : links) {
            URI url = link.getUrl();
            Long linkId = link.getId();

            GitHubLinkData linkRepoData = gitHubParserService.getLinkData(url);
            String owner = linkRepoData.owner();
            String repo = linkRepoData.repo();

            if (!owner.isBlank() && !repo.isBlank()) {
                RepositoryResponse response = gitHubClient.fetchRepository(owner, repo);
                List<RepositoryResponse> responsesInRepo = gitHubResponseRepository.findByLinkId(linkId);

                if (responsesInRepo.isEmpty()) {
                    gitHubResponseRepository.add(response, linkId);
                }

                if (isNeedToUpdate(response, link)) {
                    requests.add(getUpdateRepo(response, linkId, url));
                }
            }
            OffsetDateTime lastApiCheck = OffsetDateTime.now(ZoneOffset.UTC);
            linkRepository.updateLastApiCheck(lastApiCheck, linkId);
        }

        return requests;
    }

    private boolean isNeedToUpdate(RepositoryResponse response, Link link) {
        return response.getPushedAt().isAfter(link.getLastUpdate());
    }

    private LinkUpdateResponse getUpdateRepo(RepositoryResponse response, Long linkId, URI url) {
        OffsetDateTime updatedAt = response.getPushedAt().with(ZoneOffset.UTC);
        gitHubResponseRepository.update(response, linkId);
        linkRepository.updateLastUpdate(updatedAt, linkId);
        List<Long> tgChatIdsForUpdate = chatLinkRepository.findTgChatIds(linkId);

        return new LinkUpdateResponse(url, UPDATE_DESCRIPTION, tgChatIdsForUpdate);
    }
}
