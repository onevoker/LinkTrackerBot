package dto.response;

import java.net.URI;

public record LinkResponse(
    long id,
    URI url) {
}
