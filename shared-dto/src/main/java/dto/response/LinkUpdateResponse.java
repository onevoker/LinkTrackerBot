package dto.response;

import java.net.URI;
import java.util.List;

public record LinkUpdateResponse(

    URI url,

    String description,

    List<Long> tgChatIds) {
}
