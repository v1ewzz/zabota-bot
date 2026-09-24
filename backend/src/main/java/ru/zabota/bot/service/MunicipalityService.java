package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.UUID;

/*
 * Сервис для работы с муниципальными образованиями.
 *
 * Отвечает за CRUD-операции с Municipality,
 * разрешение связанных Region и DictionaryValue
 * по идентификаторам и проверку существования ресурсов.
 *
 * Сервис не содержит HTTP-логики и не выполняет
 * преобразование Entity в DTO самостоятельно.
 */
@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final RegionRepository regionRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final MunicipalityMapper municipalityMapper;

    public MunicipalityService(
            MunicipalityRepository municipalityRepository,
            RegionRepository regionRepository,
            DictionaryValueRepository dictionaryValueRepository,
            MunicipalityMapper municipalityMapper
    ) {
        this.municipalityRepository = municipalityRepository;
        this.regionRepository = regionRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.municipalityMapper = municipalityMapper;
    }

    @Transactional
    public MunicipalityResponse create(MunicipalityRequest request) {
        Region region = getRegion(request.getRegionId());
        DictionaryValue type = getDictionaryValue(request.getTypeId());

        Municipality municipality = municipalityMapper.toEntity(
                request,
                region,
                type
        );

        municipality = municipalityRepository.save(municipality);

        return municipalityMapper.toResponse(municipality);
    }

    @Transactional(readOnly = true)
    public MunicipalityResponse getById(UUID id) {
        Municipality municipality = municipalityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Муниципалитет с id " + id + " не найден"
                        )
                );

        return municipalityMapper.toResponse(municipality);
    }

    @Transactional(readOnly = true)
    public List<MunicipalityResponse> getAll() {
        return municipalityRepository.findAll()
                .stream()
                .map(municipalityMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MunicipalityResponse> getByRegion(UUID regionId) {
        getRegion(regionId);

        return municipalityRepository
                .findAllByRegion_RegionId(regionId)
                .stream()
                .map(municipalityMapper::toResponse)
                .toList();
    }

    @Transactional
    public MunicipalityResponse update(
            UUID id,
            MunicipalityRequest request
    ) {
        Municipality municipality = municipalityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Муниципалитет с id " + id + " не найден"
                        )
                );

        Region region = getRegion(request.getRegionId());
        DictionaryValue type = getDictionaryValue(request.getTypeId());

        municipalityMapper.updateEntity(
                municipality,
                request,
                region,
                type
        );

        municipality = municipalityRepository.save(municipality);

        return municipalityMapper.toResponse(municipality);
    }

    @Transactional
    public void delete(UUID id) {
        Municipality municipality = municipalityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Муниципалитет с id " + id + " не найден"
                        )
                );

        municipalityRepository.delete(municipality);
    }

    private Region getRegion(UUID regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Регион с id " + regionId + " не найден"
                        )
                );
    }

    private DictionaryValue getDictionaryValue(UUID id) {
        if (id == null) {
            return null;
        }

        return dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Значение справочника с id " + id + " не найдено"
                        )
                );
    }
}