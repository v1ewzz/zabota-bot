package ru.zabota.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.region.RegionRequest;
import ru.zabota.bot.dto.region.RegionResponse;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.RegionMapper;
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
 * Unit-тест RegionService.
 *
 * Проверяет все основные CRUD-сценарии:
 *
 * - создание;
 * - получение по id;
 * - получение списка;
 * - обновление;
 * - удаление;
 * - обработка отсутствующего региона.
 *
 * Spring-контекст и база данных не используются.
 * Repository и Mapper заменяются Mockito-моками.
 */
@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private RegionMapper regionMapper;

    @InjectMocks
    private RegionService regionService;

    @Test
    void shouldCreateRegion() {
        UUID regionId = UUID.randomUUID();

        RegionRequest request = createRequest();

        Region region = new Region();

        RegionResponse expected =
                createResponse(regionId);

        when(regionMapper.toEntity(request))
                .thenReturn(region);

        when(regionRepository.save(region))
                .thenReturn(region);

        when(regionMapper.toResponse(region))
                .thenReturn(expected);

        RegionResponse result =
                regionService.create(request);

        assertEquals(expected, result);

        verify(regionMapper).toEntity(request);
        verify(regionRepository).save(region);
        verify(regionMapper).toResponse(region);
    }

    @Test
    void shouldGetRegionById() {
        UUID regionId = UUID.randomUUID();

        Region region = new Region();

        RegionResponse expected =
                createResponse(regionId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(regionMapper.toResponse(region))
                .thenReturn(expected);

        RegionResponse result =
                regionService.getById(regionId);

        assertEquals(expected, result);

        verify(regionRepository).findById(regionId);
        verify(regionMapper).toResponse(region);
    }

    @Test
    void shouldThrowExceptionWhenRegionNotFound() {
        UUID regionId = UUID.randomUUID();

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> regionService.getById(regionId)
        );

        verify(regionRepository).findById(regionId);

        verify(regionMapper, never())
                .toResponse(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    @Test
    void shouldGetAllRegions() {
        Region first = new Region();
        Region second = new Region();

        RegionResponse firstResponse =
                createResponse(UUID.randomUUID());

        RegionResponse secondResponse =
                createResponse(UUID.randomUUID());

        when(regionRepository.findAll())
                .thenReturn(List.of(first, second));

        when(regionMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(regionMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<RegionResponse> result =
                regionService.getAll();

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(regionRepository).findAll();
        verify(regionMapper).toResponse(first);
        verify(regionMapper).toResponse(second);
    }

    @Test
    void shouldUpdateRegion() {
        UUID regionId = UUID.randomUUID();

        RegionRequest request =
                createRequest();

        Region region = new Region();

        RegionResponse expected =
                createResponse(regionId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(regionRepository.save(region))
                .thenReturn(region);

        when(regionMapper.toResponse(region))
                .thenReturn(expected);

        RegionResponse result =
                regionService.update(
                        regionId,
                        request
                );

        assertEquals(expected, result);

        verify(regionRepository).findById(regionId);

        verify(regionMapper).updateEntity(
                region,
                request
        );

        verify(regionRepository).save(region);

        verify(regionMapper).toResponse(region);
    }

    @Test
    void shouldThrowExceptionWhenRegionNotFoundOnUpdate() {
        UUID regionId = UUID.randomUUID();

        RegionRequest request =
                createRequest();

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> regionService.update(
                        regionId,
                        request
                )
        );

        verify(regionRepository).findById(regionId);

        verify(regionMapper, never())
                .updateEntity(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any()
                );

        verify(regionRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldDeleteRegion() {
        UUID regionId = UUID.randomUUID();

        Region region = new Region();

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        regionService.delete(regionId);

        verify(regionRepository).findById(regionId);
        verify(regionRepository).delete(region);
    }

    @Test
    void shouldThrowExceptionWhenRegionNotFoundOnDelete() {
        UUID regionId = UUID.randomUUID();

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> regionService.delete(regionId)
        );

        verify(regionRepository).findById(regionId);

        verify(regionRepository, never())
                .delete(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    private RegionRequest createRequest() {
        RegionRequest request =
                new RegionRequest();

        request.setName("Республика Татарстан");
        request.setCode("16");

        return request;
    }

    private RegionResponse createResponse(UUID id) {
        RegionResponse response =
                new RegionResponse();

        response.setRegionId(id);
        response.setName("Республика Татарстан");
        response.setCode("16");

        return response;
    }
}