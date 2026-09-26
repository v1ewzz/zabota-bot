package ru.zabota.bot.exception;

/*
 * Исключение возникает, когда запрошенный ресурс отсутствует.
 *
 * Используется сервисным слоем и преобразуется глобальным
 * обработчиком в HTTP 404 NOT FOUND.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}