package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ModelsMapper;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaChatLinkRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaGitHubResponseRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaQuestionResponseRepository;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaChatEntityRepository;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaChatLinkEntityRepository;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaLinkEntityRepository;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaQuestionResponseEntityRepository;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaRepositoryResponseEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfig {
    @Autowired
    JpaChatEntityRepository chatEntityRepository;
    @Autowired
    JpaChatLinkEntityRepository chatLinkEntityRepository;
    @Autowired
    JpaLinkEntityRepository linkEntityRepository;
    @Autowired
    JpaQuestionResponseEntityRepository questionResponseEntityRepository;
    @Autowired
    JpaRepositoryResponseEntityRepository repositoryResponseEntityRepository;
    @Autowired
    ModelsMapper mapper;

    @Bean
    public ChatLinkRepository jpaChatLinkRepository() {
        return new JpaChatLinkRepository(chatLinkEntityRepository, mapper);
    }

    @Bean
    public ChatRepository jpaChatRepository() {
        return new JpaChatRepository(mapper, chatEntityRepository, chatLinkEntityRepository);
    }

    @Bean
    public GitHubResponseRepository jpaGitHubResponseRepository() {
        return new JpaGitHubResponseRepository(repositoryResponseEntityRepository, mapper);
    }

    @Bean
    public LinkRepository jpaLinkRepository() {
        return new JpaLinkRepository(linkEntityRepository, mapper);
    }

    @Bean
    public QuestionResponseRepository jpaQuestionResponseRepository() {
        return new JpaQuestionResponseRepository(questionResponseEntityRepository, mapper);
    }
}
