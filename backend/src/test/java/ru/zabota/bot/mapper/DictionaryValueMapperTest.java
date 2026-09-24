package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import ru.zabota.bot.dto.dictionary.DictionaryValueResponse;
import ru.zabota.bot.entity.DictionaryType;
import ru.zabota.bot.entity.DictionaryValue;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Unit-тест DictionaryValueMapper.
 *
 * Проверяет преобразование DictionaryValue в
 * DictionaryValueResponse без запуска Spring-контекста.
 */
class DictionaryValueMapperTest {

    private final DictionaryValueMapper mapper = new DictionaryValueMapper();

    @Test
    void shouldMapDictionaryValueToResponse() {
        UUID valueId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();

        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setDictionaryTypeId(typeId);
        dictionaryType.setCode("MILITARY_STATUS");
        dictionaryType.setName("Военный статус");

        DictionaryValue dictionaryValue = new DictionaryValue();
        dictionaryValue.setDictionaryValueId(valueId);
        dictionaryValue.setDictionaryType(dictionaryType);
        dictionaryValue.setCode("MOBILIZED");
        dictionaryValue.setLabel("Мобилизованный");
        dictionaryValue.setSortOrder((short) 1);
        dictionaryValue.setActive(true);

        DictionaryValueResponse response = mapper.toResponse(dictionaryValue);

        assertEquals(valueId, response.getDictionaryValueId());

        assertEquals(typeId, response.getDictionaryTypeId());
        assertEquals("MILITARY_STATUS", response.getDictionaryTypeCode());
        assertEquals("Военный статус", response.getDictionaryTypeName());

        assertEquals("MOBILIZED", response.getCode());
        assertEquals("Мобилизованный", response.getLabel());
        assertEquals((short) 1, response.getSortOrder());
        assertTrue(response.isActive());
    }

    @Test
    void shouldMapDictionaryValueWithoutDictionaryType() {
        DictionaryValue dictionaryValue = new DictionaryValue();

        dictionaryValue.setCode("MOBILIZED");
        dictionaryValue.setLabel("Мобилизованный");
        dictionaryValue.setActive(true);

        DictionaryValueResponse response = mapper.toResponse(dictionaryValue);

        assertEquals("MOBILIZED", response.getCode());
        assertEquals("Мобилизованный", response.getLabel());
        assertTrue(response.isActive());

        assertNull(response.getDictionaryTypeId());
        assertNull(response.getDictionaryTypeCode());
        assertNull(response.getDictionaryTypeName());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        DictionaryValueResponse response = mapper.toResponse(null);

        assertNull(response);
    }
}