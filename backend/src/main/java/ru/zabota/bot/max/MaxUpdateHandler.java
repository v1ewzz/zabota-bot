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

                Это «Забота» — сервис для поиска доступных мер социальной поддержки.

                Вы можете открыть приложение и пройти короткую анкету,
                чтобы подобрать меры поддержки, которые соответствуют
                вашей ситуации.

                Также можно узнать подробнее о проекте.
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
                «Забота» — это сервис для персонального подбора
                мер социальной поддержки.

                Мы создаём его для людей и семей, которым может быть
                сложно самостоятельно разобраться в большом количестве
                информации о доступных мерах поддержки.

                Особенно важно это для семей военнослужащих, участников СВО
                и других категорий граждан, для которых доступные меры
                поддержки могут зависеть от конкретной жизненной ситуации,
                региона проживания, состава семьи и других условий.

                Сегодня информация о мерах поддержки может находиться
                в разных нормативных актах, государственных ресурсах
                и региональных источниках. Из-за этого человеку бывает
                сложно быстро понять:

                • какие меры поддержки могут ему подходить;
                • соответствует ли он необходимым условиям;
                • какие документы необходимо подготовить;
                • куда нужно обращаться для получения помощи.

                «Забота» должна объединить эту информацию в одном сервисе
                и сделать её понятнее для обычного пользователя.

                Пользователь заполняет информацию о своей ситуации,
                после чего система анализирует подходящие меры поддержки
                и показывает их в персонализированном виде.

                Наша цель — помочь человеку быстрее разобраться
                в доступной поддержке, понять свои дальнейшие действия
                и не тратить время на самостоятельный поиск информации
                по множеству различных источников.

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