package edu.java.scrapper.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLink {
    private Long id;
    private Long chatId;
    private Long linkId;

    public ChatLink(Long chatId, Long linkId) {
        this.chatId = chatId;
        this.linkId = linkId;
    }
}
