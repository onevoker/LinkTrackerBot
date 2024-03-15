package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import java.util.List;

public interface GitHubResponseRepository {
    void add(RepositoryResponse response, Long linkId);

    List<RepositoryResponse> findAll();

    List<RepositoryResponse> findByLinkId(Long linkId);

    void update(RepositoryResponse response, Long linkId);
}
