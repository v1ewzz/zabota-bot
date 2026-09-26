package ru.zabota.bot.exception;

/*
 * Исключение для некорректного входного запроса,
 * который не является ошибкой сервера.
 *
 * Преобразуется глобальным обработчиком
 * в HTTP 400 BAD REQUEST.
 */
public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }
}