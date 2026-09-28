package ru.zabota.bot.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.client.MaxUploadAPI;
import ru.max.botapi.model.FileAttachmentRequest;
import ru.max.botapi.model.FileUploadedInfo;
import ru.max.botapi.model.MediaRequestPayload;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.UploadEndpoint;
import ru.max.botapi.model.UploadType;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;
import ru.zabota.bot.exception.BusinessException;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/*
 * Сервис формирования и отправки PDF-сводки персонального подбора мер.
 *
 * Причина появления: кнопка "Печать / сохранить PDF" на фронтенде
 * использует window.print() — стандартный браузерный диалог печати,
 * которого нет (или он заблокирован) внутри WebView мессенджера MAX.
 * Поэтому внутри MAX сводка формируется на backend и отправляется
 * пользователю ботом прямо в чат как файл, а не через системный диалог
 * печати браузера.
 */
@Service
public class PdfSummaryService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfSummaryService.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final UserService userService;
    private final MaxBotAPI maxBotAPI;
    private final MaxUploadAPI maxUploadAPI;
    private final String maxBotUsername;

    public PdfSummaryService(
            UserService userService,
            MaxBotAPI maxBotAPI,
            MaxUploadAPI maxUploadAPI,
            @Value("${MAX_BOT_USERNAME:}") String maxBotUsername
    ) {
        this.userService = userService;
        this.maxBotAPI = maxBotAPI;
        this.maxUploadAPI = maxUploadAPI;
        this.maxBotUsername = maxBotUsername;
    }

    /**
     * Генерирует PDF-сводку по сохранённому профилю пользователя и
     * отправляет её в чат MAX.
     *
     * @param userId    внутренний идентификатор пользователя Zabota
     * @param maxUserId идентификатор пользователя в MAX (из
     *                  WebApp.initDataUnsafe.user.id на фронте); нужен,
     *                  чтобы бот понимал, в какой чат отправлять файл
     */
    public void generateAndSend(UUID userId, Long maxUserId) {
        UserProfileResponse profile = userService.getProfile(userId);
        byte[] pdf = renderPdf(profile);

        if (maxUserId == null) {
            LOG.warn(
                    "PDF-сводка сформирована (userId={}), но maxUserId не передан — "
                            + "отправка в чат MAX невозможна.",
                    userId
            );
            throw new BusinessException(
                    "Не удалось определить чат MAX для отправки файла. "
                            + "Откройте мини-приложение заново и повторите попытку."
            );
        }

        sendToMaxChat(maxUserId, pdf, profile);
    }

    private byte[] renderPdf(UserProfileResponse profile) {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, buffer);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            Paragraph title = new Paragraph("Забота — персональный список мер поддержки", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(12f);
            document.add(title);

            String fullName = profile.getUser() != null
                    ? String.join(
                            " ",
                            safe(profile.getUser().getFirstName()),
                            safe(profile.getUser().getLastName())
                    ).trim()
                    : "";

            Paragraph meta = new Paragraph(
                    (fullName.isEmpty() ? "Пользователь" : fullName)
                            + " · сформировано "
                            + java.time.LocalDate.now().format(DATE_FORMAT),
                    bodyFont
            );
            meta.setSpacingAfter(16f);
            document.add(meta);

            List<UserSupportResponse> supports = profile.getSupports();

            if (supports == null || supports.isEmpty()) {
                document.add(new Paragraph("Подходящих мер пока нет.", bodyFont));
            } else {
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{3f, 1.2f, 1.2f});

                addHeaderCell(table, "Мера поддержки", headFont);
                addHeaderCell(table, "Размер", headFont);
                addHeaderCell(table, "Статус", headFont);

                for (UserSupportResponse support : supports) {
                    table.addCell(new PdfPCell(new Paragraph(safe(support.getName()), bodyFont)));
                    table.addCell(new PdfPCell(new Paragraph(
                            support.getAmount() != null ? support.getAmount() + " \u20bd" : "\u2014",
                            bodyFont
                    )));
                    table.addCell(new PdfPCell(new Paragraph(safe(support.getStatusName()), bodyFont)));
                }

                document.add(table);
            }

            Paragraph disclaimer = new Paragraph(
                    "\nСводка предназначена для навигации и не заменяет официальное решение "
                            + "органа, предоставляющего меру поддержки.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8)
            );
            disclaimer.setSpacingBefore(16f);
            document.add(disclaimer);

            document.close();
            return buffer.toByteArray();
        } catch (Exception exception) {
            throw new BusinessException("Не удалось сформировать PDF-сводку: " + exception.getMessage());
        }
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(new java.awt.Color(240, 240, 250));
        table.addCell(cell);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private void sendToMaxChat(Long maxUserId, byte[] pdf, UserProfileResponse profile) {
        try {
            UploadEndpoint endpoint = maxBotAPI
                    .getUploadUrl(UploadType.FILE)
                    .execute();

            FileUploadedInfo uploaded = maxUploadAPI.uploadFile(
                    endpoint,
                    pdf,
                    "zabota-svodka.pdf"
            );

            FileAttachmentRequest attachment = new FileAttachmentRequest(
                    new MediaRequestPayload(uploaded.token())
            );

            NewMessageBody message = new NewMessageBody(
                    "Забота — ваша персональная сводка мер поддержки",
                    List.of(attachment),
                    null,
                    null,
                    null
            );

            maxBotAPI.sendMessage(message)
                    .userId(maxUserId)
                    .execute();

            LOG.info(
                    "PDF-сводка отправлена в чат MAX: userId={}, {} байт",
                    maxUserId,
                    pdf.length
            );

        } catch (BusinessException exception) {
            throw exception;

        } catch (RuntimeException exception) {
            LOG.error(
                    "Failed to send PDF-сводка to MAX chat: userId={}",
                    maxUserId,
                    exception
            );
            throw new BusinessException(
                    "Не удалось отправить PDF-сводку в чат MAX. "
                            + "Попробуйте ещё раз."
            );
        }
    }
}
