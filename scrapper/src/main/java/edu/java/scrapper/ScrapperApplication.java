package edu.java.scrapper;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.resourcesConfig.ResourcesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ResourcesConfig.class})
@EnableScheduling
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
