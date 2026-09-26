package ru.zabota.bot.dto.dictionary;

import java.util.UUID;

/*
 * DTO для возврата значения справочника.
 *
 * Содержит собственные данные значения и информацию
 * о связанном типе справочника.
 */
public class DictionaryValueResponse {

    private UUID dictionaryValueId;

    private UUID dictionaryTypeId;
    private String dictionaryTypeCode;
    private String dictionaryTypeName;

    private String code;
    private String label;
    private Short sortOrder;
    private boolean active;

    public DictionaryValueResponse() {
    }

    public UUID getDictionaryValueId() {
        return dictionaryValueId;
    }

    public void setDictionaryValueId(UUID dictionaryValueId) {
        this.dictionaryValueId = dictionaryValueId;
    }

    public UUID getDictionaryTypeId() {
        return dictionaryTypeId;
    }

    public void setDictionaryTypeId(UUID dictionaryTypeId) {
        this.dictionaryTypeId = dictionaryTypeId;
    }

    public String getDictionaryTypeCode() {
        return dictionaryTypeCode;
    }

    public void setDictionaryTypeCode(String dictionaryTypeCode) {
        this.dictionaryTypeCode = dictionaryTypeCode;
    }

    public String getDictionaryTypeName() {
        return dictionaryTypeName;
    }

    public void setDictionaryTypeName(String dictionaryTypeName) {
        this.dictionaryTypeName = dictionaryTypeName;
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