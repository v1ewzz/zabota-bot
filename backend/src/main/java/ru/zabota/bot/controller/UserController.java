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
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.service.UserService;

import java.util.List;
import java.util.UUID;

/*
 * REST-контроллер для работы с пользователями.
 *
 * Отвечает только за HTTP-уровень:
 *
 * - принимает запросы;
 * - валидирует UserRequest;
 * - вызывает UserService;
 * - возвращает DTO и HTTP-статусы.
 *
 * Работа с Repository, связанными сущностями, детьми
 * и бизнес-логикой находится в сервисном слое.
 */
@RestController
@RequestMapping("/api/users")
@Tag(
        name = "Users",
        description = "Операции с пользователями и их профилями"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать пользователя",
            description = "Создаёт новый профиль пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион, муниципалитет или значение справочника не найдено"
            )
    })
    public UserResponse create(
            @Valid @RequestBody UserRequest request
    ) {
        return userService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей успешно получен"
            )
    })
    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя",
            description = "Возвращает пользователя по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    public UserResponse getById(
            @Parameter(
                    description = "Идентификатор пользователя",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return userService.getById(id);
    }

    @GetMapping("/{id}/profile")
    @Operation(
            summary = "Получить профиль пользователя",
            description = "Возвращает пользователя вместе со списком его детей"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Профиль пользователя найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    public UserProfileResponse getProfile(
            @Parameter(
                    description = "Идентификатор пользователя",
                    required = true
            )
            @PathVariable UUID id
    ) {
        return userService.getProfile(id);
    }

    @PutMapping("/{id}/profile")
    @Operation(
            summary = "Обновить анкету и повторить подбор",
            description = "Полностью сохраняет новую анкету и сразу пересчитывает персональные меры поддержки"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Анкета обновлена, подбор пересчитан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь, регион, муниципалитет или значение справочника не найдено"
            )
    })
    public UserProfileResponse updateProfileAndRematch(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UserRequest request
    ) {
        return userService.updateAndRematch(id, request);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет профиль пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно обновлён"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь, регион, муниципалитет или значение справочника не найдено"
            )
    })
    public UserResponse update(
            @Parameter(
                    description = "Идентификатор пользователя",
                    required = true
            )
            @PathVariable UUID id,
            @Valid @RequestBody UserRequest request
    ) {
        return userService.update(
                id,
                request
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет профиль пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователя нельзя удалить из-за связанных данных"
            )
    })
    public void delete(
            @Parameter(
                    description = "Идентификатор пользователя",
                    required = true
            )
            @PathVariable UUID id
    ) {
        userService.delete(id);
    }
}