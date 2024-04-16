package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.clients.exceptions.RemovedLinkException;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import edu.java.scrapper.linkParser.dto.StackOverflowLinkQuestionData;
import edu.java.scrapper.linkParser.services.StackOverflowParserService;
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
public class StackOverflowUpdaterService implements ResourceUpdaterService {
    private final ApplicationConfig applicationConfig;
    private final StackOverflowParserService stackOverflowParserService;
    private final QuestionResponseRepository questionResponseRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final StackOverflowClient stackOverflowClient;
    private static final String UPDATE_DESCRIPTION = "Появилось обновление";
    private static final String ANSWERED_DESCRIPTION = "На вопрос ответили";
    private static final String ANSWER_COUNT_DESCRIPTION = "Был добавлен ответ на вопрос";
    private static final String LINK_WAS_REMOVED_BY_AUTHOR_DESCRIPTION =
        "Вы перестали ослеживать данную ссылку, так как она была удалена автором";

    @Transactional
    @Override
    public Mono<LinkUpdateResponse> getLinkUpdateResponse(Link link) {
        URI url = link.getUrl();
        Long linkId = link.getId();
        StackOverflowLinkQuestionData linkQuestionData = stackOverflowParserService.getLinkData(url);
        long questionId = linkQuestionData.questionId();

        if (questionId != 0L) {
            return stackOverflowClient.fetchQuestion(questionId)
                .flatMap(response -> {
                    Item responseItem = response.items().getFirst();
                    List<Item> responsesInRepo = questionResponseRepository.findByLinkId(linkId);

                    if (responsesInRepo.isEmpty()) {
                        questionResponseRepository.add(responseItem, linkId);
                        responsesInRepo = questionResponseRepository.findByLinkId(linkId);
                    }

                    Item questionInRepo = responsesInRepo.getFirst();
                    if (isNeedToUpdate(responseItem, link)) {
                        return Mono.just(getUpdateQuestion(responseItem, linkId, url, questionInRepo));
                    }

                    OffsetDateTime lastApiCheck = OffsetDateTime.now(ZoneOffset.UTC);
                    linkRepository.updateLastApiCheck(lastApiCheck, linkId);

                    return Mono.empty();
                })
                .onErrorResume(
                    RemovedLinkException.class,
                    exception -> Mono.just(removeLinkInDatabaseAndGetResponse(linkId, url))
                );
        }

        return Mono.empty();
    }

    @Override
    public String getSupportedLinksDomain() {
        return applicationConfig.stackOverflowDomain();
    }

    private boolean isNeedToUpdate(Item responseItem, Link link) {
        return responseItem.getLastActivityDate().isAfter(link.getLastUpdate());
    }

    private LinkUpdateResponse getUpdateQuestion(Item responseItem, Long linkId, URI url, Item questionInRepo) {
        OffsetDateTime lastEditDate = responseItem.getLastActivityDate();
        questionResponseRepository.update(responseItem, linkId);
        linkRepository.updateLastUpdate(lastEditDate, linkId);
        List<Long> tgChatIdsForUpdate = chatLinkRepository.findTgChatIds(linkId);
        String messageForUser = getUpdateMessage(responseItem, questionInRepo);

        return new LinkUpdateResponse(url, messageForUser, tgChatIdsForUpdate);
    }

    private String getUpdateMessage(Item responseItem, Item questionInRepo) {
        StringBuilder updateMessage = new StringBuilder(UPDATE_DESCRIPTION);

        if (responseItem.getAnswerCount() != questionInRepo.getAnswerCount()) {
            updateMessage.append("\n").append(ANSWER_COUNT_DESCRIPTION);
        }
        if (responseItem.getAnswered() != questionInRepo.getAnswered()) {
            updateMessage.append("\n").append(ANSWERED_DESCRIPTION);
        }

        return updateMessage.toString();
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
