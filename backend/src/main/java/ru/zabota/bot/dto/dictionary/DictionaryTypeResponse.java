package ru.zabota.bot.dto.dictionary;

import java.util.UUID;

/*
 * DTO для возврата информации о типе справочника.
 *
 * Представляет сам справочник и используется API
 * при получении доступных типов справочных данных.
 */
public class DictionaryTypeResponse {

    private UUID dictionaryTypeId;
    private String code;
    private String name;

    public DictionaryTypeResponse() {
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