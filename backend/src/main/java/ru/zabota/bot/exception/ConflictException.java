package ru.zabota.bot.exception;

/*
 * Исключение для конфликта с текущим состоянием системы.
 *
 * Например, при попытке создать запись, нарушающую
 * уникальное ограничение.
 *
 * Преобразуется глобальным обработчиком
 * в HTTP 409 CONFLICT.
 */
public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }
}