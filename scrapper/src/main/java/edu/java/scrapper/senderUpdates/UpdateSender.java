package edu.java.scrapper.senderUpdates;

import edu.java.scrapper.dto.response.LinkUpdateResponse;
import reactor.core.publisher.Mono;

public interface UpdateSender {
    Mono<Void> sendUpdate(LinkUpdateResponse update);
}
