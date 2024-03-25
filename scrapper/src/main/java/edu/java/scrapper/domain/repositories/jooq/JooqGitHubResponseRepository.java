package edu.java.scrapper.domain.repositories.jooq;

import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.scrapper.domain.repositories.jooq.generated.Tables.REPOSITORY_RESPONSE;

@RequiredArgsConstructor
public class JooqGitHubResponseRepository implements GitHubResponseRepository {
    private final DSLContext dsl;

    @Override
    public void add(RepositoryResponse response, Long linkId) {
        dsl.insertInto(
                REPOSITORY_RESPONSE,
                REPOSITORY_RESPONSE.ID,
                REPOSITORY_RESPONSE.LINK_ID,
                REPOSITORY_RESPONSE.PUSHED_AT
            )
            .values(response.getId(), linkId, response.getPushedAt())
            .execute();
    }

    @Override
    public List<RepositoryResponse> findAll() {
        return dsl.selectFrom(REPOSITORY_RESPONSE)
            .fetchInto(RepositoryResponse.class);
    }

    @Override
    public List<RepositoryResponse> findByLinkId(Long linkId) {
        return dsl.selectFrom(REPOSITORY_RESPONSE)
            .where(REPOSITORY_RESPONSE.LINK_ID.eq(linkId))
            .fetchInto(RepositoryResponse.class);
    }

    @Override
    public void update(RepositoryResponse response, Long linkId) {
        dsl.update(REPOSITORY_RESPONSE)
            .set(REPOSITORY_RESPONSE.PUSHED_AT, response.getPushedAt())
            .where(REPOSITORY_RESPONSE.LINK_ID.eq(linkId))
            .execute();
    }
}
