package ru.zabota.bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность региона.
 *
 * Соответствует таблице region в PostgreSQL.
 * Хранит идентификатор региона, его название, код
 * и дату создания записи.
 *
 * Структура сущности полностью соответствует V1__create_region.sql.
 */
@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "region_id", nullable = false, updatable = false)
    private UUID regionId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "code", length = 50)
    private String code;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Region() {
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

/*
 * Конец описания сущности Region.
 */