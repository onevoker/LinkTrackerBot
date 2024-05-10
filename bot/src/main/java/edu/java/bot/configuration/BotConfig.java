package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    public TelegramBot bot() {
        return new TelegramBot(applicationConfig.telegramToken());
    }
}
