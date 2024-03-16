package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import edu.java.scrapper.dto.stackOverflowDto.QuestionResponse;
import edu.java.scrapper.linkWorkers.LinkParserUtil;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowUpdaterService implements ResourceUpdaterService {
    private final QuestionResponseRepository questionResponseRepository;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final StackOverflowClient stackOverflowClient;
    private static final String UPDATE_DESCRIPTION = "Появилось обновление";
    private static final String ANSWERED_DESCRIPTION = "На вопрос был получен ответ";
    private static final String ANSWER_COUNT_DESCRIPTION = "Был добавлен ответ на вопрос";

    @Override
    public List<LinkUpdateRequest> getUpdates(List<Link> links) {
        List<LinkUpdateRequest> requests = new ArrayList<>();

        for (var link : links) {
            URI url = link.getUrl();
            Long linkId = link.getId();
            long questionId = LinkParserUtil.getQuestionId(url);

            if (questionId != 0L) {
                QuestionResponse response = stackOverflowClient.fetchQuestion(questionId);
                Item responseItem = response.items().getFirst();
                List<Item> responsesInRepo = questionResponseRepository.findByLinkId(linkId);

                if (responsesInRepo.isEmpty()) {
                    questionResponseRepository.add(responseItem, linkId);
                    responsesInRepo = questionResponseRepository.findByLinkId(linkId);
                }

                Item questionInRepo = responsesInRepo.getFirst();
                if (isNeedToUpdate(responseItem, link)) {
                    requests.add(getUpdateQuestion(responseItem, linkId, url, questionInRepo));
                }
            }
            OffsetDateTime lastApiCheck = OffsetDateTime.now(ZoneOffset.UTC);
            linkRepository.updateLastApiCheck(lastApiCheck, linkId);
        }

        return requests;
    }

    private boolean isNeedToUpdate(Item responseItem, Link link) {
        return responseItem.getLastActivityDate().isAfter(link.getLastUpdate());
    }

    private LinkUpdateRequest getUpdateQuestion(Item responseItem, Long linkId, URI url, Item questionInRepo) {
        OffsetDateTime lastEditDate = responseItem.getLastActivityDate();
        questionResponseRepository.update(responseItem, linkId);
        linkRepository.updateLastUpdate(lastEditDate, linkId);
        List<Long> tgChatIdsForUpdate = chatLinkRepository.findTgChatIds(linkId);
        String messageForUser = getUpdateMessage(responseItem, questionInRepo);

        return new LinkUpdateRequest(url, messageForUser, tgChatIdsForUpdate);
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
