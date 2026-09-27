package ru.zabota.bot.max;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.core.UpdateHandler;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackAnswer;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.InlineKeyboardAttachment;
import ru.max.botapi.model.InlineKeyboardAttachmentRequest;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.OpenAppButton;
import ru.max.botapi.model.Update;

@Component
public class MaxUpdateHandler implements UpdateHandler {

    private static final Logger LOG =
            LoggerFactory.getLogger(MaxUpdateHandler.class);

    private static final String INFO_CALLBACK = "info";

    private final MaxBotAPI maxBotAPI;
    private final String maxBotUsername;

    public MaxUpdateHandler(
            MaxBotAPI maxBotAPI,
            @Value("${MAX_BOT_USERNAME}") String maxBotUsername
    ) {
        this.maxBotAPI = maxBotAPI;
        this.maxBotUsername = maxBotUsername;
    }

    @Override
    public void onUpdate(Update update) {
        switch (update) {
            case BotStartedUpdate started ->
                    handleBotStarted(started);

            case MessageCreatedUpdate message ->
                    handleMessageCreated(message);

            case MessageCallbackUpdate callback ->
                    handleMessageCallback(callback);

            default ->
                    LOG.debug(
                            "MAX update ignored: {}",
                            update.updateType()
                    );
        }
    }

    private void handleBotStarted(BotStartedUpdate update) {
        long chatId = update.chatId();

        sendWelcomeMessage(chatId);

        LOG.info(
                "MAX bot started: chatId={}, userId={}",
                chatId,
                update.user().userId()
        );
    }

    private void handleMessageCreated(MessageCreatedUpdate update) {
        if (update.message() == null || update.message().body() == null) {
            return;
        }

        String text = update.message().body().text();

        if (text == null || text.isBlank()) {
            return;
        }

        Long chatId = update.message().recipient().chatId();

        if (chatId == null) {
            return;
        }

        switch (text.trim().toLowerCase()) {
            case "/start" ->
                    sendWelcomeMessage(chatId);

            case "/info" ->
                    sendInfoMessage(chatId);

            default ->
                    LOG.debug(
                            "MAX message ignored: chatId={}, text={}",
                            chatId,
                            text
                    );
        }
    }

    private void handleMessageCallback(MessageCallbackUpdate update) {
        if (update.callback() == null) {
            return;
        }

        String payload = update.callback().payload();

        if (!INFO_CALLBACK.equals(payload)) {
            return;
        }

        if (update.message() == null) {
            return;
        }

        Long chatId = update.message().recipient().chatId();

        if (chatId == null) {
            return;
        }

        sendInfoMessage(chatId);

        answerCallback(
                update.callback().callbackId()
        );
    }

    private void sendWelcomeMessage(long chatId) {
        String text = """
                Здравствуйте!

                Это «Забота» — сервис, созданный для помощи
                военнослужащим, участникам СВО и их семьям.

                Мы хотим сделать получение информации о доступной
                социальной поддержке более понятным, удобным
                и быстрым.

                Откройте приложение и пройдите короткую анкету,
                чтобы получить персональный подбор мер поддержки
                по вашей жизненной ситуации.

                Подробнее о проекте можно узнать по кнопке «Инфо».
                """;

        List<List<Button>> buttons = List.of(
                List.of(
                        OpenAppButton.ofWebApp(
                                "Старт",
                                maxBotUsername
                        ),
                        new CallbackButton(
                                "Инфо",
                                INFO_CALLBACK,
                                null
                        )
                )
        );

        InlineKeyboardAttachmentRequest keyboard =
                new InlineKeyboardAttachmentRequest(
                        new InlineKeyboardAttachment.KeyboardPayload(
                                buttons
                        )
                );

        NewMessageBody message =
                new NewMessageBody(
                        text,
                        List.of(keyboard),
                        null,
                        null,
                        null
                );

        sendMessage(chatId, message);
    }

    private void sendInfoMessage(long chatId) {
        String text = """
                «Забота» — проект, созданный для того, чтобы
                военнослужащим, участникам СВО и их семьям
                было проще получать необходимую информацию
                о доступной социальной поддержке.

                Мы хотим сделать повседневные вопросы, связанные
                с поиском помощи и оформлением поддержки,
                более понятными и удобными.

                Человеку не должно приходиться самостоятельно
                искать информацию по множеству сайтов,
                нормативных документов и различных организаций,
                пытаясь понять, что именно ему доступно.

                «Забота» собирает необходимые сведения в одном
                месте и помогает разобраться в ситуации
                простым и понятным способом.

                Что делает сервис:

                - проводит короткую анкету о вашей ситуации;
                - учитывает регион проживания и состав семьи;
                - подбирает подходящие меры поддержки;
                - показывает условия получения каждой меры;
                - указывает необходимые документы;
                - объясняет, куда и как обращаться;
                - показывает подходящие МФЦ и другие необходимые места;
                - позволяет сохранять интересующие меры поддержки;
                - помогает отслеживать уже рассмотренные и полученные меры.

                Главная задача «Заботы» — не просто показать список
                возможной помощи, а помочь человеку понять,
                что именно ему подходит и какие действия нужно
                выполнить дальше.

                Мы стремимся сделать сервис удобным для использования
                в реальной жизни: чтобы нужная информация была
                собрана в одном месте, была понятна человеку
                и не требовала долгого самостоятельного поиска.

                Для начала работы откройте приложение кнопкой «Старт».
                """;

        sendMessage(
                chatId,
                new NewMessageBody(
                        text,
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    private void sendMessage(
            long chatId,
            NewMessageBody message
    ) {
        try {
            maxBotAPI.sendMessage(message)
                    .chatId(chatId)
                    .execute();

        } catch (RuntimeException exception) {
            LOG.error(
                    "Failed to send MAX message: chatId={}",
                    chatId,
                    exception
            );
        }
    }

    private void answerCallback(String callbackId) {
        try {
            maxBotAPI.answerOnCallback(
                    new CallbackAnswer(
                            null,
                            "Информация отправлена"
                    ),
                    callbackId
            ).execute();

        } catch (RuntimeException exception) {
            LOG.error(
                    "Failed to answer MAX callback: callbackId={}",
                    callbackId,
                    exception
            );
        }
    }
}