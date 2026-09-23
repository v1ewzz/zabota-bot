package ru.zabota.bot.exception;

/*
 * Исключение для нарушения бизнес-правил приложения.
 *
 * Используется сервисным слоем, когда операция технически
 * корректна, но запрещена правилами предметной области.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
