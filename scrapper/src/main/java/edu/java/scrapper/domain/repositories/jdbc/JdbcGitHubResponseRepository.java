package edu.java.scrapper.domain.repositories.jdbc;

import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class JdbcGitHubResponseRepository implements GitHubResponseRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(RepositoryResponse response, Long linkId) {
        jdbcTemplate.update(
            "INSERT INTO repository_response VALUES (?, ?, ?)",
            response.getId(),
            linkId,
            response.getPushedAt()
        );
    }

    @Override
    public List<RepositoryResponse> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM repository_response",
            new BeanPropertyRowMapper<>(RepositoryResponse.class)
        );
    }

    @Override
    public List<RepositoryResponse> findByLinkId(Long linkId) {
        return jdbcTemplate.query(
            "SELECT * FROM repository_response WHERE link_id = ?",
            new BeanPropertyRowMapper<>(RepositoryResponse.class),
            linkId
        );
    }

    @Override
    public void update(RepositoryResponse response, Long linkId) {
        jdbcTemplate.update(
            "UPDATE repository_response SET pushed_at = ? WHERE link_id = ?",
            response.getPushedAt(),
            linkId
        );
    }
}
