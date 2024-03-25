package edu.java.scrapper.domain.models;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    private Long id;
    private URI url;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime lastApiCheck;

    public Link(URI url, OffsetDateTime lastUpdate) {
        this.url = url;
        this.lastUpdate = lastUpdate;
    }

    public Link(URI url, OffsetDateTime lastUpdate, OffsetDateTime lastApiCheck) {
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.lastApiCheck = lastApiCheck;
    }
}
