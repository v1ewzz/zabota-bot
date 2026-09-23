package ru.zabota.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.municipality.MunicipalityRequest;
import ru.zabota.bot.dto.municipality.MunicipalityResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.MunicipalityMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.MunicipalityRepository;
import ru.zabota.bot.repository.RegionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit-тест MunicipalityService.
 *
 * Проверяет CRUD-операции, получение муниципалитетов
 * по региону и обработку отсутствующих связанных ресурсов.
 *
 * Spring-контекст и PostgreSQL не запускаются.
 * Repository и Mapper заменены Mockito-моками.
 */
@ExtendWith(MockitoExtension.class)
class MunicipalityServiceTest {

    @Mock
    private MunicipalityRepository municipalityRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private MunicipalityMapper municipalityMapper;

    @InjectMocks
    private MunicipalityService municipalityService;

    @Test
    void shouldCreateMunicipality() {
        UUID regionId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        MunicipalityRequest request = createRequest(
                regionId,
                typeId
        );

        Region region = createRegion(regionId);
        DictionaryValue type = createType(typeId);

        Municipality municipality = new Municipality();
        MunicipalityResponse expected = createResponse(municipalityId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.of(type));

        when(municipalityMapper.toEntity(
                request,
                region,
                type
        )).thenReturn(municipality);

        when(municipalityRepository.save(municipality))
                .thenReturn(municipality);

        when(municipalityMapper.toResponse(municipality))
                .thenReturn(expected);

        MunicipalityResponse result =
                municipalityService.create(request);

        assertEquals(expected, result);

        verify(regionRepository).findById(regionId);
        verify(dictionaryValueRepository).findById(typeId);
        verify(municipalityMapper).toEntity(
                request,
                region,
                type
        );
        verify(municipalityRepository).save(municipality);
        verify(municipalityMapper).toResponse(municipality);
    }

    @Test
    void shouldCreateMunicipalityWithoutType() {
        UUID regionId = UUID.randomUUID();

        MunicipalityRequest request = createRequest(
                regionId,
                null
        );

        Region region = createRegion(regionId);

        Municipality municipality = new Municipality();
        MunicipalityResponse expected = createResponse(
                UUID.randomUUID()
        );

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityMapper.toEntity(
                request,
                region,
                null
        )).thenReturn(municipality);

        when(municipalityRepository.save(municipality))
                .thenReturn(municipality);

        when(municipalityMapper.toResponse(municipality))
                .thenReturn(expected);

        MunicipalityResponse result =
                municipalityService.create(request);

        assertEquals(expected, result);

        verify(regionRepository).findById(regionId);
        verify(dictionaryValueRepository, never())
                .findById(org.mockito.ArgumentMatchers.any());
        verify(municipalityRepository).save(municipality);
    }

    @Test
    void shouldThrowExceptionWhenRegionNotFoundOnCreate() {
        UUID regionId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();

        MunicipalityRequest request =
                createRequest(regionId, typeId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> municipalityService.create(request)
        );

        verify(regionRepository).findById(regionId);
        verify(dictionaryValueRepository, never())
                .findById(typeId);
        verify(municipalityRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowExceptionWhenTypeNotFoundOnCreate() {
        UUID regionId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();

        MunicipalityRequest request =
                createRequest(regionId, typeId);

        Region region = createRegion(regionId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> municipalityService.create(request)
        );

        verify(regionRepository).findById(regionId);
        verify(dictionaryValueRepository).findById(typeId);
        verify(municipalityRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldGetMunicipalityById() {
        UUID id = UUID.randomUUID();

        Municipality municipality = new Municipality();
        MunicipalityResponse expected = createResponse(id);

        when(municipalityRepository.findById(id))
                .thenReturn(Optional.of(municipality));

        when(municipalityMapper.toResponse(municipality))
                .thenReturn(expected);

        MunicipalityResponse result =
                municipalityService.getById(id);

        assertEquals(expected, result);

        verify(municipalityRepository).findById(id);
        verify(municipalityMapper).toResponse(municipality);
    }

    @Test
    void shouldThrowExceptionWhenMunicipalityNotFound() {
        UUID id = UUID.randomUUID();

        when(municipalityRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> municipalityService.getById(id)
        );

        verify(municipalityRepository).findById(id);
        verify(municipalityMapper, never())
                .toResponse(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldGetAllMunicipalities() {
        Municipality first = new Municipality();
        Municipality second = new Municipality();

        MunicipalityResponse firstResponse =
                createResponse(UUID.randomUUID());

        MunicipalityResponse secondResponse =
                createResponse(UUID.randomUUID());

        when(municipalityRepository.findAll())
                .thenReturn(List.of(first, second));

        when(municipalityMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(municipalityMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<MunicipalityResponse> result =
                municipalityService.getAll();

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(municipalityRepository).findAll();
        verify(municipalityMapper).toResponse(first);
        verify(municipalityMapper).toResponse(second);
    }

    @Test
    void shouldGetMunicipalitiesByRegion() {
        UUID regionId = UUID.randomUUID();

        Region region = createRegion(regionId);

        Municipality first = new Municipality();
        Municipality second = new Municipality();

        MunicipalityResponse firstResponse =
                createResponse(UUID.randomUUID());

        MunicipalityResponse secondResponse =
                createResponse(UUID.randomUUID());

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository
                .findAllByRegion_RegionId(regionId))
                .thenReturn(List.of(first, second));

        when(municipalityMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(municipalityMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<MunicipalityResponse> result =
                municipalityService.getByRegion(regionId);

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(regionRepository).findById(regionId);
        verify(municipalityRepository)
                .findAllByRegion_RegionId(regionId);
    }

    @Test
    void shouldThrowExceptionWhenRegionNotFoundOnGetByRegion() {
        UUID regionId = UUID.randomUUID();

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> municipalityService.getByRegion(regionId)
        );

        verify(regionRepository).findById(regionId);
        verify(municipalityRepository, never())
                .findAllByRegion_RegionId(regionId);
    }

    @Test
    void shouldUpdateMunicipality() {
        UUID municipalityId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();

        MunicipalityRequest request =
                createRequest(regionId, typeId);

        Municipality municipality = new Municipality();
        Region region = createRegion(regionId);
        DictionaryValue type = createType(typeId);

        MunicipalityResponse expected =
                createResponse(municipalityId);

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.of(municipality));

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.of(type));

        when(municipalityRepository.save(municipality))
                .thenReturn(municipality);

        when(municipalityMapper.toResponse(municipality))
                .thenReturn(expected);

        MunicipalityResponse result =
                municipalityService.update(
                        municipalityId,
                        request
                );

        assertEquals(expected, result);

        verify(municipalityRepository)
                .findById(municipalityId);

        verify(regionRepository).findById(regionId);
        verify(dictionaryValueRepository).findById(typeId);

        verify(municipalityMapper).updateEntity(
                municipality,
                request,
                region,
                type
        );

        verify(municipalityRepository).save(municipality);
    }

    @Test
    void shouldDeleteMunicipality() {
        UUID id = UUID.randomUUID();

        Municipality municipality = new Municipality();

        when(municipalityRepository.findById(id))
                .thenReturn(Optional.of(municipality));

        municipalityService.delete(id);

        verify(municipalityRepository).findById(id);
        verify(municipalityRepository).delete(municipality);
    }

    @Test
    void shouldThrowExceptionWhenMunicipalityNotFoundOnDelete() {
        UUID id = UUID.randomUUID();

        when(municipalityRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> municipalityService.delete(id)
        );

        verify(municipalityRepository).findById(id);
        verify(municipalityRepository, never())
                .delete(org.mockito.ArgumentMatchers.any());
    }

    private MunicipalityRequest createRequest(
            UUID regionId,
            UUID typeId
    ) {
        MunicipalityRequest request =
                new MunicipalityRequest();

        request.setRegionId(regionId);
        request.setName("Казань");
        request.setDistrict("город Казань");
        request.setTypeId(typeId);

        return request;
    }

    private Region createRegion(UUID id) {
        Region region = new Region();

        region.setRegionId(id);
        region.setName("Республика Татарстан");

        return region;
    }

    private DictionaryValue createType(UUID id) {
        DictionaryValue type =
                new DictionaryValue();

        type.setDictionaryValueId(id);
        type.setCode("CITY");
        type.setLabel("Город");

        return type;
    }

    private MunicipalityResponse createResponse(UUID id) {
        MunicipalityResponse response =
                new MunicipalityResponse();

        response.setMunicipalityId(id);
        response.setName("Казань");

        return response;
    }
}