package edu.java.scrapper.dto.response;

import java.net.URI;

public record LinkResponse(
    long id,
    URI url) {
}
