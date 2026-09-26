package ru.zabota.bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.zabota.bot.dto.support.SupportMeasureRequest;
import ru.zabota.bot.dto.support.SupportMeasureResponse;
import ru.zabota.bot.service.SupportMeasureService;

import java.util.List;
import java.util.UUID;

/*
 * REST-контроллер для работы с мерами социальной поддержки.
 *
 * Отвечает только за HTTP-уровень:
 *
 * - принимает HTTP-запросы;
 * - валидирует SupportMeasureRequest;
 * - вызывает SupportMeasureService;
 * - возвращает DTO и HTTP-статусы.
 *
 * Бизнес-логика, работа с Repository и разрешение
 * связанных сущностей находятся в сервисном слое.
 */
@RestController
@RequestMapping("/api/support-measures")
@Tag(
        name = "Support Measures",
        description = "Операции с мерами социальной поддержки"
)
public class SupportMeasureController {

    private final SupportMeasureService supportMeasureService;

    public SupportMeasureController(
            SupportMeasureService supportMeasureService
    ) {
        this.supportMeasureService = supportMeasureService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать меру поддержки",
            description = "Создаёт новую меру социальной поддержки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Мера поддержки успешно создана"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Связанное значение справочника не найдено"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт данных"
            )
    })
    public SupportMeasureResponse create(
            @Valid @RequestBody SupportMeasureRequest request
    ) {
        return supportMeasureService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Получить все меры поддержки",
            description = "Возвращает список всех мер социальной поддержки"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список мер поддержки успешно получен"
    )
    public List<SupportMeasureResponse> getAll() {
        return supportMeasureService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить меру поддержки",
            description = "Возвращает меру социальной поддержки по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Мера поддержки найдена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мера поддержки не найдена"
            )
    })
    public SupportMeasureResponse getById(
            @Parameter(
                    description = "Идентификатор меры поддержки",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return supportMeasureService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить меру поддержки",
            description = "Обновляет существующую меру социальной поддержки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Мера поддержки успешно обновлена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мера поддержки или связанное значение справочника не найдены"
            )
    })
    public SupportMeasureResponse update(
            @Parameter(
                    description = "Идентификатор меры поддержки",
                    required = true
            )
            @PathVariable UUID id,
            @Valid @RequestBody SupportMeasureRequest request
    ) {
        return supportMeasureService.update(
                id,
                request
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить меру поддержки",
            description = "Удаляет меру социальной поддержки по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Мера поддержки успешно удалена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мера поддержки не найдена"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Меру поддержки нельзя удалить из-за связанных данных"
            )
    })
    public void delete(
            @Parameter(
                    description = "Идентификатор меры поддержки",
                    required = true
            )
            @PathVariable UUID id
    ) {
        supportMeasureService.delete(id);
    }
}