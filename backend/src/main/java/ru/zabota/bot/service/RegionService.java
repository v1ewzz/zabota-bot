package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.region.RegionRequest;
import ru.zabota.bot.dto.region.RegionResponse;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.RegionMapper;
import ru.zabota.bot.repository.RegionRepository;

import java.util.List;
import java.util.UUID;

/*
 * Сервис для работы с регионами.
 *
 * Отвечает за CRUD-операции с сущностью Region:
 *
 * - создание региона;
 * - получение региона по идентификатору;
 * - получение всех регионов;
 * - обновление региона;
 * - удаление региона.
 *
 * Сервис не содержит HTTP-логики и не работает
 * с DTO напрямую после передачи данных Mapper.
 */
@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    public RegionService(
            RegionRepository regionRepository,
            RegionMapper regionMapper
    ) {
        this.regionRepository = regionRepository;
        this.regionMapper = regionMapper;
    }

    @Transactional
    public RegionResponse create(RegionRequest request) {
        Region region = regionMapper.toEntity(request);

        region = regionRepository.save(region);

        return regionMapper.toResponse(region);
    }

    @Transactional(readOnly = true)
    public RegionResponse getById(UUID id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Регион с id " + id + " не найден"
                        )
                );

        return regionMapper.toResponse(region);
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> getAll() {
        return regionRepository.findAll()
                .stream()
                .map(regionMapper::toResponse)
                .toList();
    }

    @Transactional
    public RegionResponse update(
            UUID id,
            RegionRequest request
    ) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Регион с id " + id + " не найден"
                        )
                );

        regionMapper.updateEntity(region, request);

        region = regionRepository.save(region);

        return regionMapper.toResponse(region);
    }

    @Transactional
    public void delete(UUID id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Регион с id " + id + " не найден"
                        )
                );

        regionRepository.delete(region);
    }
}