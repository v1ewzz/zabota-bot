package ru.zabota.bot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/*
 * Конфигурация OpenAPI и Swagger UI.
 *
 * Определяет основную информацию о REST API приложения
 * и описание схемы авторизации.
 *
 * На текущем этапе авторизация в API ещё не реализована,
 * поэтому security-схема добавлена как задел на будущее.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Zabota Bot API",
                version = "1.0.0",
                description = "REST API сервиса персонального подбора мер социальной поддержки"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}