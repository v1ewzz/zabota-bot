package ru.zabota.bot.dto.region;

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * DTO для возврата информации о регионе.
 *
 * Содержит идентификатор региона, основные данные
 * и дату создания записи.
 */
public class RegionResponse {

    private UUID regionId;
    private String name;
    private String code;
    private LocalDateTime createdAt;

    public RegionResponse() {
    }

    public RegionResponse(
            UUID regionId,
            String name,
            String code,
            LocalDateTime createdAt
    ) {
        this.regionId = regionId;
        this.name = name;
        this.code = code;
        this.createdAt = createdAt;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void setRegionId(UUID regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}