package edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService;

import dto.response.LinkUpdateResponse;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.clients.exceptions.RemovedLinkException;
import edu.java.scrapper.configuration.resourcesConfig.ClientsConfig;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import edu.java.scrapper.dto.stackOverflowDto.QuestionResponse;
import edu.java.scrapper.linkParser.dto.StackOverflowLinkQuestionData;
import edu.java.scrapper.linkParser.services.StackOverflowParserService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StackOverflowUpdaterService implements ResourceUpdaterService {
    private final ClientsConfig.StackOverflow stackOverflow;
    private final StackOverflowParserService stackOverflowParserService;
    private final QuestionResponseRepository questionResponseRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final StackOverflowClient stackOverflowClient;
    private final RemoverLinksService removerLinksService;
    private static final String UPDATE_DESCRIPTION = "Появилось обновление";
    private static final String ANSWERED_DESCRIPTION = "На вопрос ответили";
    private static final String ANSWER_COUNT_DESCRIPTION = "Был добавлен ответ на вопрос";

    @Transactional
    @Override
    public LinkUpdateResponse getLinkUpdateResponse(Link link) {
        LinkUpdateResponse updateResponse = null;
        URI url = link.getUrl();
        Long linkId = link.getId();
        StackOverflowLinkQuestionData linkQuestionData = stackOverflowParserService.getLinkData(url);
        long questionId = linkQuestionData.questionId();

        if (questionId != 0L) {
            try {
                QuestionResponse response = stackOverflowClient.fetchQuestion(questionId);
                Item responseItem = response.items().getFirst();
                List<Item> responsesInRepo = questionResponseRepository.findByLinkId(linkId);

                if (responsesInRepo.isEmpty()) {
                    questionResponseRepository.add(responseItem, linkId);
                    responsesInRepo = questionResponseRepository.findByLinkId(linkId);
                }

                Item questionInRepo = responsesInRepo.getFirst();
                if (isNeedToUpdate(responseItem, link)) {
                    updateResponse = getUpdateQuestion(responseItem, linkId, url, questionInRepo);
                }

                OffsetDateTime lastApiCheck = OffsetDateTime.now(ZoneOffset.UTC);
                linkRepository.updateLastApiCheck(lastApiCheck, linkId);
            } catch (RemovedLinkException exception) {
                updateResponse = removerLinksService.removeLinkInDatabaseAndGetResponse(linkId, url);
            }
        }

        return updateResponse;
    }

    @Override
    public String getSupportedLinksDomain() {
        return stackOverflow.urls().domain();
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
}
