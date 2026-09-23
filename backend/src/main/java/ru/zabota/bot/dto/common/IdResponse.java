package ru.zabota.bot.dto.common;

import java.util.UUID;

/*
 * Универсальный ответ с идентификатором созданного или изменённого ресурса.
 *
 * Используется API в ситуациях, когда клиенту достаточно получить
 * идентификатор созданной сущности без полного объекта.
 */
public class IdResponse {

    private UUID id;

    public IdResponse() {
    }

    public IdResponse(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}