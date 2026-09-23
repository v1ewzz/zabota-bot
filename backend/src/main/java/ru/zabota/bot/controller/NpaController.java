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
import ru.zabota.bot.dto.npa.NpaRequest;
import ru.zabota.bot.dto.npa.NpaResponse;
import ru.zabota.bot.service.NpaService;

import java.util.List;
import java.util.UUID;

/*
 * REST-контроллер для работы с нормативно-правовыми актами.
 *
 * Отвечает за HTTP-уровень:
 *
 * - принимает запросы;
 * - валидирует NpaRequest;
 * - передаёт данные в NpaService;
 * - возвращает NpaResponse;
 * - определяет HTTP-статусы ответов.
 *
 * Бизнес-логика, поиск связанных DictionaryValue
 * и работа с Repository находятся в сервисном слое.
 */
@RestController
@RequestMapping("/api/npas")
@Tag(
        name = "NPA",
        description = "Операции с нормативно-правовыми актами"
)
public class NpaController {

    private final NpaService npaService;

    public NpaController(NpaService npaService) {
        this.npaService = npaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать НПА",
            description = "Создаёт новый нормативно-правовой акт"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "НПА успешно создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Связанное значение справочника не найдено"
            )
    })
    public NpaResponse create(
            @Valid @RequestBody NpaRequest request
    ) {
        return npaService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Получить все НПА",
            description = "Возвращает список всех нормативно-правовых актов"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список НПА успешно получен"
    )
    public List<NpaResponse> getAll() {
        return npaService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить НПА",
            description = "Возвращает нормативно-правовой акт по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "НПА найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "НПА не найден"
            )
    })
    public NpaResponse getById(
            @Parameter(
                    description = "Идентификатор НПА",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return npaService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить НПА",
            description = "Обновляет существующий нормативно-правовой акт"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "НПА успешно обновлён"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "НПА или связанное значение справочника не найдено"
            )
    })
    public NpaResponse update(
            @Parameter(
                    description = "Идентификатор НПА",
                    required = true
            )
            @PathVariable UUID id,
            @Valid @RequestBody NpaRequest request
    ) {
        return npaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить НПА",
            description = "Удаляет нормативно-правовой акт по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "НПА успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "НПА не найден"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "НПА нельзя удалить из-за связанных данных"
            )
    })
    public void delete(
            @Parameter(
                    description = "Идентификатор НПА",
                    required = true
            )
            @PathVariable UUID id
    ) {
        npaService.delete(id);
    }
}