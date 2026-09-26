package ru.zabota.bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import ru.zabota.bot.dto.region.RegionRequest;
import ru.zabota.bot.dto.region.RegionResponse;
import ru.zabota.bot.service.RegionService;

import java.util.List;
import java.util.UUID;

/*
 * REST-контроллер для работы с регионами.
 *
 * Отвечает только за HTTP-уровень:
 *
 * - принимает HTTP-запросы;
 * - валидирует RegionRequest;
 * - передаёт данные в RegionService;
 * - возвращает HTTP-ответы.
 *
 * Бизнес-логика, работа с Repository и преобразование
 * Entity в DTO находятся за пределами контроллера.
 */
@RestController
@RequestMapping("/api/regions")
@Tag(
        name = "Regions",
        description = "Операции с регионами"
)
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать регион",
            description = "Создаёт новый регион"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Регион успешно создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Object.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт данных"
            )
    })
    public RegionResponse create(
            @Valid @RequestBody RegionRequest request
    ) {
        return regionService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Получить все регионы",
            description = "Возвращает список всех регионов"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список регионов успешно получен"
    )
    public List<RegionResponse> getAll() {
        return regionService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить регион",
            description = "Возвращает регион по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Регион найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион не найден"
            )
    })
    public RegionResponse getById(
            @Parameter(
                    description = "Идентификатор региона",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return regionService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить регион",
            description = "Обновляет существующий регион"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Регион успешно обновлён"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион не найден"
            )
    })
    public RegionResponse update(
            @Parameter(
                    description = "Идентификатор региона",
                    required = true
            )
            @PathVariable UUID id,
            @Valid @RequestBody RegionRequest request
    ) {
        return regionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить регион",
            description = "Удаляет регион по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Регион успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион не найден"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Регион нельзя удалить из-за связанных данных"
            )
    })
    public void delete(
            @Parameter(
                    description = "Идентификатор региона",
                    required = true
            )
            @PathVariable UUID id
    ) {
        regionService.delete(id);
    }
}