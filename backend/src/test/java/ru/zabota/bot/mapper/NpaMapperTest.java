package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import ru.zabota.bot.dto.npa.NpaResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Npa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit-тест NpaMapper.
 *
 * Проверяет корректность преобразования Npa
 * в NpaResponse без запуска Spring-контекста.
 */
class NpaMapperTest {

    private final NpaMapper mapper = new NpaMapper();

    @Test
    void shouldMapNpaToResponse() {
        UUID npaId = UUID.randomUUID();
        UUID npaTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        LocalDate adoptionDate = LocalDate.of(2025, 1, 15);
        LocalDate validFrom = LocalDate.of(2025, 2, 1);
        LocalDate validTo = LocalDate.of(2027, 2, 1);

        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 20, 10, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 2, 10, 12, 45);

        DictionaryValue npaType = new DictionaryValue();
        npaType.setDictionaryValueId(npaTypeId);
        npaType.setCode("FEDERAL_LAW");
        npaType.setLabel("Федеральный закон");

        DictionaryValue level = new DictionaryValue();
        level.setDictionaryValueId(levelId);
        level.setCode("FEDERAL");
        level.setLabel("Федеральный");

        DictionaryValue status = new DictionaryValue();
        status.setDictionaryValueId(statusId);
        status.setCode("ACTIVE");
        status.setLabel("Действует");

        Npa npa = new Npa();
        npa.setNpaId(npaId);
        npa.setName("О мерах социальной поддержки");
        npa.setNpaType(npaType);
        npa.setNumber("123-ФЗ");
        npa.setAdoptionDate(adoptionDate);
        npa.setValidFrom(validFrom);
        npa.setValidTo(validTo);
        npa.setLevel(level);
        npa.setStatus(status);
        npa.setOfficialUrl("https://example.ru/law");
        npa.setCreatedAt(createdAt);
        npa.setUpdatedAt(updatedAt);

        NpaResponse response = mapper.toResponse(npa);

        assertEquals(npaId, response.getNpaId());
        assertEquals("О мерах социальной поддержки", response.getName());

        assertEquals(npaTypeId, response.getNpaTypeId());
        assertEquals("FEDERAL_LAW", response.getNpaTypeCode());
        assertEquals("Федеральный закон", response.getNpaTypeName());

        assertEquals("123-ФЗ", response.getNumber());

        assertEquals(adoptionDate, response.getAdoptionDate());
        assertEquals(validFrom, response.getValidFrom());
        assertEquals(validTo, response.getValidTo());

        assertEquals(levelId, response.getLevelId());
        assertEquals("FEDERAL", response.getLevelCode());
        assertEquals("Федеральный", response.getLevelName());

        assertEquals(statusId, response.getStatusId());
        assertEquals("ACTIVE", response.getStatusCode());
        assertEquals("Действует", response.getStatusName());

        assertEquals("https://example.ru/law", response.getOfficialUrl());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    void shouldMapNpaWithoutOptionalReferences() {
        Npa npa = new Npa();

        npa.setName("Тестовый НПА");
        npa.setNumber("1");
        npa.setOfficialUrl("https://example.ru");

        NpaResponse response = mapper.toResponse(npa);

        assertEquals("Тестовый НПА", response.getName());
        assertEquals("1", response.getNumber());
        assertEquals("https://example.ru", response.getOfficialUrl());

        assertNull(response.getNpaTypeId());
        assertNull(response.getNpaTypeCode());
        assertNull(response.getNpaTypeName());

        assertNull(response.getLevelId());
        assertNull(response.getLevelCode());
        assertNull(response.getLevelName());

        assertNull(response.getStatusId());
        assertNull(response.getStatusCode());
        assertNull(response.getStatusName());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        NpaResponse response = mapper.toResponse(null);

        assertNull(response);
    }
}