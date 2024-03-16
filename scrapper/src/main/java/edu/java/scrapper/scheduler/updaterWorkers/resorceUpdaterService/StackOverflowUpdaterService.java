package edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService;

import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.repositories.QuestionResponseRepository;
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
                if (isNeedToUpdate(responseItem, questionInRepo)) {
                    requests.add(getUpdateQuestion(responseItem, linkId, url));
                }
            }
            OffsetDateTime lastApiCheck = OffsetDateTime.now().with(ZoneOffset.UTC);
            linkRepository.updateLastApiCheck(lastApiCheck, linkId);
        }

        return requests;
    }

    private boolean isNeedToUpdate(Item responseItem, Item questionInRepo) {
        return responseItem.getLastEditDate().isAfter(questionInRepo.getLastEditDate());
    }

    private LinkUpdateRequest getUpdateQuestion(Item responseItem, Long linkId, URI url) {
        OffsetDateTime lastEditDate = responseItem.getLastEditDate();
        questionResponseRepository.update(responseItem, linkId);
        linkRepository.updateLastUpdate(lastEditDate, linkId);
        List<Long> tgChatIdsForUpdate = chatLinkRepository.findTgChatIds(linkId);

        return new LinkUpdateRequest(url, UPDATE_DESCRIPTION, tgChatIdsForUpdate);
    }
}
