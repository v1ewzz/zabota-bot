package ru.zabota.bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.client.MaxUploadAPI;

@Configuration
public class MaxBotConfig {

    @Bean(destroyMethod = "close")
    public MaxBotAPI maxBotAPI(
            @Value("${MAX_BOT_TOKEN}") String token
    ) {
        return MaxBotAPI.create(token);
    }

    @Bean(destroyMethod = "close")
    public MaxUploadAPI maxUploadAPI(MaxBotAPI maxBotAPI) {
        return new MaxUploadAPI(maxBotAPI.serializer());
    }
}