package edu.java.scrapper.senderUpdates;

import dto.response.LinkUpdateResponse;

public interface UpdateSender {
    void sendUpdate(LinkUpdateResponse update);
}
