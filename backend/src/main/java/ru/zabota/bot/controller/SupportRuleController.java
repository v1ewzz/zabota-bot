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
import ru.zabota.bot.dto.support.SupportRuleRequest;
import ru.zabota.bot.dto.support.SupportRuleResponse;
import ru.zabota.bot.service.SupportRuleService;

import java.util.List;
import java.util.UUID;

/*
 * REST-контроллер для работы с правилами мер социальной поддержки.
 *
 * Отвечает только за HTTP-уровень:
 *
 * - принимает HTTP-запросы;
 * - валидирует SupportRuleRequest;
 * - вызывает SupportRuleService;
 * - возвращает DTO и HTTP-статусы.
 *
 * Проверка условий правил и алгоритм подбора мер
 * находятся в SupportMatchingService.
 */
@RestController
@RequestMapping("/api/support-rules")
@Tag(
        name = "Support Rules",
        description = "Операции с правилами мер социальной поддержки"
)
public class SupportRuleController {

    private final SupportRuleService supportRuleService;

    public SupportRuleController(
            SupportRuleService supportRuleService
    ) {
        this.supportRuleService = supportRuleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать правило",
            description = "Создаёт новое правило меры социальной поддержки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Правило успешно создано"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мера поддержки или оператор не найдены"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт данных"
            )
    })
    public SupportRuleResponse create(
            @Valid @RequestBody SupportRuleRequest request
    ) {
        return supportRuleService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Получить все правила",
            description = "Возвращает список всех правил мер социальной поддержки"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список правил успешно получен"
    )
    public List<SupportRuleResponse> getAll() {
        return supportRuleService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить правило",
            description = "Возвращает правило по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Правило найдено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Правило не найдено"
            )
    })
    public SupportRuleResponse getById(
            @Parameter(
                    description = "Идентификатор правила",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return supportRuleService.getById(id);
    }

    @GetMapping("/support/{supportId}")
    @Operation(
            summary = "Получить правила меры",
            description = "Возвращает все правила указанной меры социальной поддержки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Правила меры успешно получены"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Мера поддержки не найдена"
            )
    })
    public List<SupportRuleResponse> getBySupportId(
            @Parameter(
                    description = "Идентификатор меры поддержки",
                    required = true
            )
            @PathVariable UUID supportId
    ) {
        return supportRuleService.getBySupportId(supportId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить правило",
            description = "Обновляет существующее правило"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Правило успешно обновлено"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Правило, мера поддержки или оператор не найдены"
            )
    })
    public SupportRuleResponse update(
            @Parameter(
                    description = "Идентификатор правила",
                    required = true
            )
            @PathVariable UUID id,
            @Valid @RequestBody SupportRuleRequest request
    ) {
        return supportRuleService.update(
                id,
                request
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить правило",
            description = "Удаляет правило по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Правило успешно удалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Правило не найдено"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Правило нельзя удалить из-за связанных данных"
            )
    })
    public void delete(
            @Parameter(
                    description = "Идентификатор правила",
                    required = true
            )
            @PathVariable UUID id
    ) {
        supportRuleService.delete(id);
    }
}