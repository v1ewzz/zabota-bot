package ru.zabota.bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zabota.bot.dto.dictionary.DictionaryTypeResponse;
import ru.zabota.bot.dto.dictionary.DictionaryValueResponse;
import ru.zabota.bot.service.DictionaryService;

import java.util.List;
import java.util.UUID;

/*
 * REST-контроллер для работы со справочниками.
 *
 * Отвечает за получение типов справочников,
 * их значений и отдельных значений справочника.
 *
 * Контроллер не изменяет данные справочников и не содержит
 * бизнес-логики. Все операции передаются DictionaryService.
 */
@RestController
@RequestMapping("/api/dictionaries")
@Tag(
        name = "Dictionaries",
        description = "Получение типов справочников и их значений"
)
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(
            DictionaryService dictionaryService
    ) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping
    @Operation(
            summary = "Получить типы справочников",
            description = "Возвращает список всех типов справочников"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Типы справочников успешно получены"
    )
    public List<DictionaryTypeResponse> getDictionaryTypes() {
        return dictionaryService.getDictionaryTypes();
    }

    @GetMapping("/by-code/{code}")
    @Operation(
            summary = "Получить справочник по коду",
            description = "Возвращает тип справочника по машинному коду"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Справочник найден"),
            @ApiResponse(responseCode = "404", description = "Справочник не найден")
    })
    public DictionaryTypeResponse getDictionaryTypeByCode(
            @PathVariable String code
    ) {
        return dictionaryService.getDictionaryTypeByCode(code);
    }

    @GetMapping("/by-code/{code}/values")
    @Operation(
            summary = "Получить активные значения справочника по коду",
            description = "Возвращает активные значения справочника по машинному коду"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Значения справочника получены"),
            @ApiResponse(responseCode = "404", description = "Справочник с указанным кодом не найден")
    })
    public List<DictionaryValueResponse> getDictionaryValuesByCode(
            @PathVariable String code
    ) {
        return dictionaryService.getDictionaryValuesByCode(code);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить тип справочника",
            description = "Возвращает тип справочника по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Тип справочника найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Тип справочника не найден"
            )
    })
    public DictionaryTypeResponse getDictionaryTypeById(
            @Parameter(
                    description = "Идентификатор типа справочника",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return dictionaryService.getDictionaryTypeById(id);
    }

    @GetMapping("/{id}/values")
    @Operation(
            summary = "Получить значения справочника",
            description = "Возвращает все значения указанного справочника"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Значения справочника успешно получены"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Справочник не найден"
            )
    })
    public List<DictionaryValueResponse> getDictionaryValues(
            @Parameter(
                    description = "Идентификатор типа справочника",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return dictionaryService.getDictionaryValues(id);
    }

    @GetMapping("/values/{id}")
    @Operation(
            summary = "Получить значение справочника",
            description = "Возвращает отдельное значение справочника по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Значение справочника найдено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Значение справочника не найдено"
            )
    })
    public DictionaryValueResponse getDictionaryValueById(
            @Parameter(
                    description = "Идентификатор значения справочника",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return dictionaryService.getDictionaryValueById(id);
    }
}