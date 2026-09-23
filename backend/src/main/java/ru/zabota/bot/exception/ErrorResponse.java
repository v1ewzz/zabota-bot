package ru.zabota.bot.exception;

import java.time.LocalDateTime;
import java.util.Map;

/*
 * Единый формат ответа API при возникновении ошибки.
 *
 * Используется GlobalExceptionHandler для формирования
 * согласованных HTTP-ответов независимо от типа исключения.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}