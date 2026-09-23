package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import ru.zabota.bot.dto.dictionary.DictionaryTypeResponse;
import ru.zabota.bot.entity.DictionaryType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit-тест DictionaryTypeMapper.
 *
 * Проверяет корректность преобразования DictionaryType
 * в DictionaryTypeResponse без запуска Spring-контекста.
 */
class DictionaryTypeMapperTest {

    private final DictionaryTypeMapper mapper = new DictionaryTypeMapper();

    @Test
    void shouldMapDictionaryTypeToResponse() {
        UUID id = UUID.randomUUID();

        DictionaryType entity = new DictionaryType();
        entity.setDictionaryTypeId(id);
        entity.setCode("MILITARY_STATUS");
        entity.setName("Военный статус");

        DictionaryTypeResponse response = mapper.toResponse(entity);

        assertEquals(id, response.getDictionaryTypeId());
        assertEquals("MILITARY_STATUS", response.getCode());
        assertEquals("Военный статус", response.getName());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        DictionaryTypeResponse response = mapper.toResponse(null);

        assertNull(response);
    }
}