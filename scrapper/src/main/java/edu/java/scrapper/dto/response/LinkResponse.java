package edu.java.scrapper.dto.response;

import java.net.URI;

public record LinkResponse(
    int id,
    URI url) {
}
