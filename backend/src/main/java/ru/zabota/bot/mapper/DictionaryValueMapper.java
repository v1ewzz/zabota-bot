package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.dictionary.DictionaryValueResponse;
import ru.zabota.bot.entity.DictionaryType;
import ru.zabota.bot.entity.DictionaryValue;

/*
 * Маппер сущности DictionaryValue.
 *
 * Отвечает за ручное преобразование DictionaryValue
 * в DictionaryValueResponse.
 */
@Component
public class DictionaryValueMapper {

    public DictionaryValueResponse toResponse(DictionaryValue dictionaryValue) {
        if (dictionaryValue == null) {
            return null;
        }

        DictionaryValueResponse response = new DictionaryValueResponse();

        response.setDictionaryValueId(dictionaryValue.getDictionaryValueId());
        response.setCode(dictionaryValue.getCode());
        response.setLabel(dictionaryValue.getLabel());
        response.setSortOrder(dictionaryValue.getSortOrder());
        response.setActive(dictionaryValue.isActive());

        DictionaryType dictionaryType = dictionaryValue.getDictionaryType();

        if (dictionaryType != null) {
            response.setDictionaryTypeId(dictionaryType.getDictionaryTypeId());
            response.setDictionaryTypeCode(dictionaryType.getCode());
            response.setDictionaryTypeName(dictionaryType.getName());
        }

        return response;
    }
}