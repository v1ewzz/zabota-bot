package ru.zabota.bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.zabota.bot.dto.pdf.PdfSummaryRequest;
import ru.zabota.bot.service.PdfSummaryService;

import java.util.UUID;

/*
 * REST-контроллер отправки PDF-сводки персонального подбора в чат MAX.
 *
 * Причина появления endpoint'а: кнопка "Печать / сохранить PDF" на
 * фронтенде вызывала window.print(), который не работает внутри WebView
 * мессенджера MAX. Внутри MAX фронтенд теперь обращается сюда, backend
 * формирует PDF по сохранённому профилю и отправляет его пользователю
 * ботом напрямую в чат.
 */
@RestController
@RequestMapping("/api/users/{userId}/pdf-summary")
@Tag(
        name = "PDF Summary",
        description = "Формирование и отправка PDF-сводки подбора мер в чат MAX"
)
public class PdfSummaryController {

    private final PdfSummaryService pdfSummaryService;

    public PdfSummaryController(PdfSummaryService pdfSummaryService) {
        this.pdfSummaryService = pdfSummaryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Отправить PDF-сводку в чат MAX",
            description = "Формирует PDF по сохранённому профилю пользователя и отправляет его ботом в чат MAX"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "PDF сформирован и отправлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "422", description = "Не удалось отправить файл (например, не определён чат MAX)")
    })
    public void sendPdfSummary(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable UUID userId,
            @RequestBody(required = false) PdfSummaryRequest request
    ) {
        Long maxUserId = request != null ? request.getMaxUserId() : null;
        pdfSummaryService.generateAndSend(userId, maxUserId);
    }
}
