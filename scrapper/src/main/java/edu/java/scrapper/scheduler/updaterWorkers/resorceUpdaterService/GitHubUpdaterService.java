package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.clients.exceptions.RemovedLinkException;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.models.ChatLink;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GitHubUpdaterService implements ResourceUpdaterService {
    private final ApplicationConfig applicationConfig;
    private final GitHubParserService gitHubParserService;
    private final GitHubResponseRepository gitHubResponseRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final GitHubClient gitHubClient;
    private static final String UPDATE_DESCRIPTION = "Появилось обновление";
    private static final String LINK_WAS_REMOVED_BY_AUTHOR_DESCRIPTION =
        "Вы перестали ослеживать данную ссылку, так как она была удалена автором";

    @Transactional
    @Override
    public Mono<LinkUpdateResponse> getLinkUpdateResponse(Link link) {
        URI url = link.getUrl();
        Long linkId = link.getId();

        GitHubLinkData linkRepoData = gitHubParserService.getLinkData(url);
        String owner = linkRepoData.owner();
        String repo = linkRepoData.repo();

        if (!owner.isBlank() && !repo.isBlank()) {
            return gitHubClient.fetchRepository(owner, repo)
                .flatMap(response -> {
                        List<RepositoryResponse> responsesInRepo = gitHubResponseRepository.findByLinkId(linkId);

                        if (responsesInRepo.isEmpty()) {
                            gitHubResponseRepository.add(response, linkId);
                        }

                        if (isNeedToUpdate(response, link)) {
                            return Mono.just(getUpdateRepo(response, linkId, url));
                        }
                        OffsetDateTime lastApiCheck = OffsetDateTime.now(ZoneOffset.UTC);
                        linkRepository.updateLastApiCheck(lastApiCheck, linkId);

                        return Mono.empty();
                    }
                )
                .onErrorResume(
                    RemovedLinkException.class,
                    exception -> Mono.just(removeLinkInDatabaseAndGetResponse(linkId, url))
                );
        }
        return Mono.empty();
    }

    @Override
    public String getSupportedLinksDomain() {
        return applicationConfig.gitHubDomain();
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

    private LinkUpdateResponse removeLinkInDatabaseAndGetResponse(Long linkId, URI url) {
        List<Long> tgChatIdsForUpdate = chatLinkRepository.findTgChatIds(linkId);

        for (Long chatId : tgChatIdsForUpdate) {
            chatLinkRepository.remove(new ChatLink(chatId, linkId));
        }
        linkRepository.remove(linkId);

        return new LinkUpdateResponse(url, LINK_WAS_REMOVED_BY_AUTHOR_DESCRIPTION, tgChatIdsForUpdate);
    }
}
