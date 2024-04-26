package edu.java.scrapper.configuration.dbAccessorsConfiguration;

import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcChatLinkRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcGitHubResponseRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcQuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public ChatLinkRepository jdbcChatLinkRepository() {
        return new JdbcChatLinkRepository(jdbcTemplate);
    }

    @Bean
    public ChatRepository jdbcChatRepository() {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public GitHubResponseRepository jdbcGitHubResponseRepository() {
        return new JdbcGitHubResponseRepository(jdbcTemplate);
    }

    @Bean
    public LinkRepository jdbcLinkRepository() {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public QuestionResponseRepository jdbcQuestionResponseRepository() {
        return new JdbcQuestionResponseRepository(jdbcTemplate);
    }
}
