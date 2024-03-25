package edu.java.scrapper.domain.repositories.jpa;

import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.modelsMapper.ModelsMapper;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.jpa.entities.LinkEntity;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaLinkEntityRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;

@Log4j2
@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository {
    private final JpaLinkEntityRepository linkEntityRepository;
    private final ModelsMapper mapper;

    @Override
    public void add(Link link) {
        try {
            LinkEntity linkEntity = new LinkEntity();
            linkEntity.setUrl(link.getUrl());
            linkEntity.setLastUpdate(link.getLastUpdate());
            linkEntity.setLastApiCheck(link.getLastApiCheck());
            linkEntityRepository.save(linkEntity);
        } catch (DataIntegrityViolationException e) {
            log.info("Добавили опять какую-то популярную ссылку)");
        }
    }

    @Override
    public void remove(Long id) {
        linkEntityRepository.deleteById(id);
    }

    @Override
    public List<Link> findAll() {
        return linkEntityRepository.findAll().stream()
            .map(mapper::getLink)
            .collect(Collectors.toList());
    }

    @Override
    public List<Link> findByUrl(URI url) {
        return linkEntityRepository.findByUrl(url).stream()
            .map(mapper::getLink)
            .collect(Collectors.toList());
    }

    @Override
    public List<Link> findOldCheckedLinks(OffsetDateTime timestamp) {
        return linkEntityRepository.findLinkEntitiesByLastApiCheckLessThan(timestamp).stream()
            .map(mapper::getLink)
            .collect(Collectors.toList());
    }

    @Override
    public void updateLastUpdate(OffsetDateTime time, Long id) {
        Optional<LinkEntity> byId = linkEntityRepository.findById(id);
        if (byId.isPresent()) {
            LinkEntity linkEntity = byId.get();
            linkEntity.setLastUpdate(time);
            linkEntityRepository.save(linkEntity);
        }
    }

    @Override
    public void updateLastApiCheck(OffsetDateTime time, Long id) {
        Optional<LinkEntity> byId = linkEntityRepository.findById(id);
        if (byId.isPresent()) {
            LinkEntity linkEntity = byId.get();
            linkEntity.setLastApiCheck(time);
            linkEntityRepository.save(linkEntity);
        }
    }
}
