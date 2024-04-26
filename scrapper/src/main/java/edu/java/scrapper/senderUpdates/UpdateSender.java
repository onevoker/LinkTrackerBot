package edu.java.scrapper.senderUpdates;

import edu.java.scrapper.dto.response.LinkUpdateResponse;

public interface UpdateSender {
    void sendUpdate(LinkUpdateResponse update);
}
