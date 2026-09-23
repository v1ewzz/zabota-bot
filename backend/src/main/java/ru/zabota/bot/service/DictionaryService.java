package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.dictionary.DictionaryTypeResponse;
import ru.zabota.bot.dto.dictionary.DictionaryValueResponse;
import ru.zabota.bot.entity.DictionaryType;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.DictionaryTypeMapper;
import ru.zabota.bot.mapper.DictionaryValueMapper;
import ru.zabota.bot.repository.DictionaryTypeRepository;
import ru.zabota.bot.repository.DictionaryValueRepository;

import java.util.List;
import java.util.UUID;

/*
 * Сервис для работы с системными справочниками.
 *
 * Отвечает за получение типов справочников и их значений.
 * Изменение справочных данных через данный сервис не выполняется.
 *
 * Сервис не содержит SQL-логики и не выполняет преобразование
 * Entity в DTO самостоятельно — для этого используются Repository
 * и Mapper соответственно.
 */
@Service
@Transactional(readOnly = true)
public class DictionaryService {

    private final DictionaryTypeRepository dictionaryTypeRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final DictionaryTypeMapper dictionaryTypeMapper;
    private final DictionaryValueMapper dictionaryValueMapper;

    public DictionaryService(
            DictionaryTypeRepository dictionaryTypeRepository,
            DictionaryValueRepository dictionaryValueRepository,
            DictionaryTypeMapper dictionaryTypeMapper,
            DictionaryValueMapper dictionaryValueMapper
    ) {
        this.dictionaryTypeRepository = dictionaryTypeRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.dictionaryTypeMapper = dictionaryTypeMapper;
        this.dictionaryValueMapper = dictionaryValueMapper;
    }

    public List<DictionaryTypeResponse> getDictionaryTypes() {
        return dictionaryTypeRepository.findAll()
                .stream()
                .map(dictionaryTypeMapper::toResponse)
                .toList();
    }

    public DictionaryTypeResponse getDictionaryTypeByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ResourceNotFoundException(
                    "Код справочника не указан"
            );
        }

        DictionaryType dictionaryType = dictionaryTypeRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Справочник с кодом "
                                        + code
                                        + " не найден"
                        )
                );

        return dictionaryTypeMapper.toResponse(dictionaryType);
    }

    public List<DictionaryValueResponse> getDictionaryValuesByCode(
            String code
    ) {
        DictionaryType dictionaryType = dictionaryTypeRepository
                .findByCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Справочник с кодом "
                                        + code
                                        + " не найден"
                        )
                );

        return dictionaryValueRepository
                .findAllByDictionaryType_DictionaryTypeId(
                        dictionaryType.getDictionaryTypeId()
                )
                .stream()
                .filter(DictionaryValue::isActive)
                .map(dictionaryValueMapper::toResponse)
                .toList();
    }

    public DictionaryTypeResponse getDictionaryTypeById(UUID id) {
        DictionaryType dictionaryType = dictionaryTypeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Тип справочника с id " + id + " не найден"
                        )
                );

        return dictionaryTypeMapper.toResponse(dictionaryType);
    }

    public List<DictionaryValueResponse> getDictionaryValues(UUID dictionaryTypeId) {
        getDictionaryTypeById(dictionaryTypeId);

        return dictionaryValueRepository
                .findAllByDictionaryType_DictionaryTypeId(dictionaryTypeId)
                .stream()
                .map(dictionaryValueMapper::toResponse)
                .toList();
    }

    public DictionaryValueResponse getDictionaryValueById(UUID id) {
        DictionaryValue dictionaryValue = dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Значение справочника с id " + id + " не найдено"
                        )
                );

        return dictionaryValueMapper.toResponse(dictionaryValue);
    }
}