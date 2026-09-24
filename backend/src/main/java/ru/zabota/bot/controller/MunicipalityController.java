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
import ru.zabota.bot.dto.municipality.MunicipalityRequest;
import ru.zabota.bot.dto.municipality.MunicipalityResponse;
import ru.zabota.bot.service.MunicipalityService;

import java.util.List;
import java.util.UUID;

/*
 * REST-контроллер для работы с муниципалитетами.
 *
 * Отвечает только за HTTP-уровень:
 *
 * - принимает запросы;
 * - валидирует MunicipalityRequest;
 * - вызывает MunicipalityService;
 * - возвращает DTO и HTTP-статусы.
 *
 * Бизнес-логика и работа с Repository находятся
 * в сервисном слое.
 */
@RestController
@RequestMapping("/api/municipalities")
@Tag(
        name = "Municipalities",
        description = "Операции с муниципалитетами"
)
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    public MunicipalityController(
            MunicipalityService municipalityService
    ) {
        this.municipalityService = municipalityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать муниципалитет",
            description = "Создаёт новый муниципалитет"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Муниципалитет успешно создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион или тип муниципалитета не найден"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Конфликт данных"
            )
    })
    public MunicipalityResponse create(
            @Valid @RequestBody MunicipalityRequest request
    ) {
        return municipalityService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Получить все муниципалитеты",
            description = "Возвращает список всех муниципалитетов"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список муниципалитетов успешно получен"
    )
    public List<MunicipalityResponse> getAll() {
        return municipalityService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить муниципалитет",
            description = "Возвращает муниципалитет по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Муниципалитет найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Муниципалитет не найден"
            )
    })
    public MunicipalityResponse getById(
            @Parameter(
                    description = "Идентификатор муниципалитета",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return municipalityService.getById(id);
    }

    @GetMapping("/by-region/{regionId}")
    @Operation(
            summary = "Получить муниципалитеты региона",
            description = "Возвращает все муниципалитеты указанного региона"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Муниципалитеты успешно получены"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион не найден"
            )
    })
    public List<MunicipalityResponse> getByRegion(
            @Parameter(
                    description = "Идентификатор региона",
                    required = true
            )
            @PathVariable UUID regionId
    ) {
        return municipalityService.getByRegion(regionId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить муниципалитет",
            description = "Обновляет существующий муниципалитет"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Муниципалитет успешно обновлён"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Муниципалитет, регион или тип не найден"
            )
    })
    public MunicipalityResponse update(
            @Parameter(
                    description = "Идентификатор муниципалитета",
                    required = true
            )
            @PathVariable UUID id,
            @Valid @RequestBody MunicipalityRequest request
    ) {
        return municipalityService.update(
                id,
                request
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить муниципалитет",
            description = "Удаляет муниципалитет по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Муниципалитет успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Муниципалитет не найден"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Муниципалитет нельзя удалить из-за связанных данных"
            )
    })
    public void delete(
            @Parameter(
                    description = "Идентификатор муниципалитета",
                    required = true
            )
            @PathVariable UUID id
    ) {
        municipalityService.delete(id);
    }
}