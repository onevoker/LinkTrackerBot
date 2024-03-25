package edu.java.scrapper.dto.gitHubDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryResponse {
    private long id;

    @JsonProperty("pushed_at")
    private OffsetDateTime pushedAt;
}


