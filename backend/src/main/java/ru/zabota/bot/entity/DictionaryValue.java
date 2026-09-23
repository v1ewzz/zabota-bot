package ru.zabota.bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

/*
 * Сущность значения справочника.
 *
 * Соответствует таблице dictionary_value в PostgreSQL.
 * Каждое значение относится к конкретному DictionaryType.
 *
 * Например:
 * DictionaryType = MILITARY_STATUS
 * DictionaryValue = MOBILIZED
 *
 * Связь с DictionaryType реализована через внешний ключ
 * dictionary_type_id.
 */
@Entity
@Table(name = "dictionary_value")
public class DictionaryValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "dictionary_value_id", nullable = false, updatable = false)
    private UUID dictionaryValueId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "dictionary_type_id",
            nullable = false
    )
    private DictionaryType dictionaryType;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "label", nullable = false, length = 255)
    private String label;

    @Column(name = "sort_order")
    private Short sortOrder;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public DictionaryValue() {
    }

    public UUID getDictionaryValueId() {
        return dictionaryValueId;
    }

    public void setDictionaryValueId(UUID dictionaryValueId) {
        this.dictionaryValueId = dictionaryValueId;
    }

    public DictionaryType getDictionaryType() {
        return dictionaryType;
    }

    public void setDictionaryType(DictionaryType dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Short getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Short sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

/*
 * Конец описания сущности DictionaryValue.
 */