package ru.zabota.bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;
import ru.zabota.bot.dto.usersupport.UserSupportUpdateRequest;
import ru.zabota.bot.service.UserSupportService;

import java.util.List;
import java.util.UUID;

/*
 * REST API личного кабинета пользователя.
 *
 * Предоставляет сохранённые результаты подбора, повторный подбор
 * по данным профиля и управление статусом конкретной меры.
 */
@RestController
@RequestMapping("/api/users/{userId}/supports")
@Tag(
        name = "User Supports",
        description = "Персональные меры поддержки и личный кабинет"
)
public class UserSupportController {

    private final UserSupportService userSupportService;

    public UserSupportController(UserSupportService userSupportService) {
        this.userSupportService = userSupportService;
    }

    @PostMapping("/search")
    @Operation(
            summary = "Повторно подобрать меры",
            description = "Запускает подбор по сохранённому профилю и сохраняет персональный результат"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Подбор выполнен и сохранён"),
            @ApiResponse(responseCode = "404", description = "Пользователь или связанная сущность не найдена")
    })
    public ResponseEntity<SupportSearchResponse> rematch(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                userSupportService.rematch(userId)
        );
    }

    @GetMapping
    @Operation(
            summary = "Получить меры пользователя",
            description = "Возвращает сохранённые персональные меры поддержки"
    )
    public List<UserSupportResponse> getAll(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable UUID userId
    ) {
        return userSupportService.getAll(userId);
    }

    @GetMapping("/{userSupportId}")
    @Operation(
            summary = "Получить персональную меру",
            description = "Возвращает одну меру из личного кабинета"
    )
    public UserSupportResponse getById(
            @PathVariable UUID userId,
            @PathVariable UUID userSupportId
    ) {
        return userSupportService.getById(
                userId,
                userSupportId
        );
    }

    @PatchMapping("/{userSupportId}")
    @Operation(
            summary = "Изменить персональную меру",
            description = "Изменяет статус оформления, признак выбора и заметку"
    )
    public UserSupportResponse update(
            @PathVariable UUID userId,
            @PathVariable UUID userSupportId,
            @Valid @RequestBody UserSupportUpdateRequest request
    ) {
        return userSupportService.update(
                userId,
                userSupportId,
                request
        );
    }
}
