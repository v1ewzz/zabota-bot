package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import ru.zabota.bot.dto.municipality.MunicipalityResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit-тест MunicipalityMapper.
 *
 * Проверяет преобразование Municipality в MunicipalityResponse
 * без запуска Spring-контекста.
 */
class MunicipalityMapperTest {

    private final MunicipalityMapper mapper = new MunicipalityMapper();

    @Test
    void shouldMapMunicipalityToResponse() {
        UUID municipalityId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();

        LocalDateTime createdAt = LocalDateTime.now();

        Region region = new Region();
        region.setRegionId(regionId);
        region.setName("Республика Татарстан");
        region.setCode("16");

        DictionaryValue type = new DictionaryValue();
        type.setDictionaryValueId(typeId);
        type.setCode("CITY");
        type.setLabel("Город");

        Municipality municipality = new Municipality();
        municipality.setMunicipalityId(municipalityId);
        municipality.setRegion(region);
        municipality.setName("Казань");
        municipality.setDistrict("город Казань");
        municipality.setType(type);
        municipality.setCreatedAt(createdAt);

        MunicipalityResponse response = mapper.toResponse(municipality);

        assertEquals(municipalityId, response.getMunicipalityId());

        assertEquals(regionId, response.getRegionId());
        assertEquals("Республика Татарстан", response.getRegionName());

        assertEquals("Казань", response.getName());
        assertEquals("город Казань", response.getDistrict());

        assertEquals(typeId, response.getTypeId());
        assertEquals("CITY", response.getTypeCode());
        assertEquals("Город", response.getTypeName());

        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    void shouldMapMunicipalityWithoutOptionalFields() {
        Municipality municipality = new Municipality();

        municipality.setName("Казань");

        MunicipalityResponse response = mapper.toResponse(municipality);

        assertEquals("Казань", response.getName());

        assertNull(response.getRegionId());
        assertNull(response.getRegionName());

        assertNull(response.getTypeId());
        assertNull(response.getTypeCode());
        assertNull(response.getTypeName());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        MunicipalityResponse response = mapper.toResponse(null);

        assertNull(response);
    }
}