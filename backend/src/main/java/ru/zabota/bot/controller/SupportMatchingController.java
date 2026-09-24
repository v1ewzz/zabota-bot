package ru.zabota.bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zabota.bot.dto.support.SupportSearchRequest;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.service.SupportMatchingService;

/*
 * REST-контроллер для персонального подбора мер социальной поддержки.
 *
 * Принимает параметры пользователя и передаёт их в
 * SupportMatchingService, который выполняет всю бизнес-логику
 * проверки доступности мер поддержки.
 *
 * Контроллер не содержит условий подбора, работы с правилами
 * или обращений к репозиториям.
 */
@RestController
@RequestMapping("/api/supports")
@Tag(
        name = "Подбор мер поддержки",
        description = "API для персонального поиска доступных мер социальной поддержки"
)
public class SupportMatchingController {

    private final SupportMatchingService supportMatchingService;

    public SupportMatchingController(
            SupportMatchingService supportMatchingService
    ) {
        this.supportMatchingService = supportMatchingService;
    }

    @PostMapping("/search")
    @Operation(
            summary = "Подобрать меры социальной поддержки",
            description = "Возвращает меры поддержки, подходящие пользователю по переданным параметрам"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Подбор успешно выполнен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион или муниципалитет не найден"
            )
    })
    public ResponseEntity<SupportSearchResponse> search(
            @Valid @RequestBody SupportSearchRequest request
    ) {
        SupportSearchResponse response =
                supportMatchingService.search(request);

        return ResponseEntity.ok(response);
    }
}