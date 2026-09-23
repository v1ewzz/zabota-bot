package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.dictionary.DictionaryTypeResponse;
import ru.zabota.bot.entity.DictionaryType;

/*
 * Маппер сущности DictionaryType.
 *
 * Отвечает за ручное преобразование JPA Entity
 * в DTO, используемый REST API.
 */
@Component
public class DictionaryTypeMapper {

    public DictionaryTypeResponse toResponse(DictionaryType dictionaryType) {
        if (dictionaryType == null) {
            return null;
        }

        DictionaryTypeResponse response = new DictionaryTypeResponse();

        response.setDictionaryTypeId(dictionaryType.getDictionaryTypeId());
        response.setCode(dictionaryType.getCode());
        response.setName(dictionaryType.getName());

        return response;
    }
}