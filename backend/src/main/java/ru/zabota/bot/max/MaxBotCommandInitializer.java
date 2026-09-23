package ru.zabota.bot.max;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.model.BotCommand;
import ru.max.botapi.model.BotCommandsPatch;

@Component
public class MaxBotCommandInitializer implements ApplicationRunner {

    private static final Logger LOG =
            LoggerFactory.getLogger(MaxBotCommandInitializer.class);

    private final MaxBotAPI maxBotAPI;

    public MaxBotCommandInitializer(MaxBotAPI maxBotAPI) {
        this.maxBotAPI = maxBotAPI;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            maxBotAPI.editMyCommands(
                    new BotCommandsPatch(
                            List.of(
                                    new BotCommand(
                                            "start",
                                            "Запустить «Заботу»"
                                    ),
                                    new BotCommand(
                                            "info",
                                            "Информация о «Заботе»"
                                    )
                            )
                    )
            ).execute();

            LOG.info(
                    "MAX bot commands registered successfully"
            );

        } catch (RuntimeException exception) {
            LOG.error(
                    "Failed to register MAX bot commands",
                    exception
            );
        }
    }
}