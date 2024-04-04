package edu.java.scrapper.configuration.dbAccessorsConfiguration;

import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.domain.repositories.jooq.JooqChatLinkRepository;
import edu.java.scrapper.domain.repositories.jooq.JooqChatRepository;
import edu.java.scrapper.domain.repositories.jooq.JooqGitHubResponseRepository;
import edu.java.scrapper.domain.repositories.jooq.JooqLinkRepository;
import edu.java.scrapper.domain.repositories.jooq.JooqQuestionResponseRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqConfig {
    @Autowired
    private DSLContext dsl;

    @Bean
    public ChatLinkRepository jooqChatLinkRepository() {
        return new JooqChatLinkRepository(dsl);
    }

    @Bean
    public ChatRepository jooqChatRepository() {
        return new JooqChatRepository(dsl);
    }

    @Bean
    public GitHubResponseRepository jooqGitHubResponseRepository() {
        return new JooqGitHubResponseRepository(dsl);
    }

    @Bean
    public LinkRepository jooqLinkRepository() {
        return new JooqLinkRepository(dsl);
    }

    @Bean
    public QuestionResponseRepository jooqQuestionResponseRepository() {
        return new JooqQuestionResponseRepository(dsl);
    }
}
