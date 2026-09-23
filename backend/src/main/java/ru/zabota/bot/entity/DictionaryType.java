package ru.zabota.bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/*
 * Сущность типа справочника.
 *
 * Соответствует таблице dictionary_type в PostgreSQL.
 * Определяет тип справочника и содержит его машинный код
 * и отображаемое название.
 *
 * Структура сущности полностью соответствует V2__create_dictionary_type.sql.
 */
@Entity
@Table(name = "dictionary_type")
public class DictionaryType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dictionary_type_id", nullable = false, updatable = false)
    private UUID dictionaryTypeId;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    public DictionaryType() {
    }

    public UUID getDictionaryTypeId() {
        return dictionaryTypeId;
    }

    public void setDictionaryTypeId(UUID dictionaryTypeId) {
        this.dictionaryTypeId = dictionaryTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
