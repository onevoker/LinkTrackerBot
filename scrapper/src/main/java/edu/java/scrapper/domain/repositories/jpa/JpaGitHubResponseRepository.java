package edu.java.scrapper.domain.repositories.jpa;

import edu.java.scrapper.domain.ModelsMapper;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.jpa.entities.RepositoryResponseEntity;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaRepositoryResponseEntityRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaGitHubResponseRepository implements GitHubResponseRepository {
    private final JpaRepositoryResponseEntityRepository repositoryResponseEntityRepository;
    private final ModelsMapper mapper;

    @Override
    public void add(RepositoryResponse response, Long linkId) {
        RepositoryResponseEntity entity = mapper.getRepositoryResponseEntity(response, linkId);
        repositoryResponseEntityRepository.save(entity);
    }

    @Override
    public List<RepositoryResponse> findAll() {
        return repositoryResponseEntityRepository.findAll().stream()
            .map(mapper::getRepositoryResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<RepositoryResponse> findByLinkId(Long linkId) {
        return repositoryResponseEntityRepository.findByLinkId(linkId).stream()
            .map(mapper::getRepositoryResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void update(RepositoryResponse response, Long linkId) {
        Optional<RepositoryResponseEntity> byId = repositoryResponseEntityRepository.findById(response.getId());
        if (byId.isPresent()) {
            RepositoryResponseEntity repositoryResponseEntity = byId.get();
            repositoryResponseEntity.setId(response.getId());
            repositoryResponseEntity.setPushedAt(response.getPushedAt());
        }
    }
}
