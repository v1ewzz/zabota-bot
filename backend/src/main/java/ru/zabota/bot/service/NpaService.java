package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.npa.NpaRequest;
import ru.zabota.bot.dto.npa.NpaResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Npa;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.NpaMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.NpaRepository;

import java.util.List;
import java.util.UUID;

/*
 * Сервис для работы с нормативно-правовыми актами.
 *
 * Отвечает за CRUD-операции с Npa и разрешение
 * связанных DictionaryValue по идентификаторам.
 *
 * Сервис не содержит HTTP-логики и не обращается
 * напрямую к DTO или Entity друг друга без Mapper.
 */
@Service
public class NpaService {

    private final NpaRepository npaRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final NpaMapper npaMapper;

    public NpaService(
            NpaRepository npaRepository,
            DictionaryValueRepository dictionaryValueRepository,
            NpaMapper npaMapper
    ) {
        this.npaRepository = npaRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.npaMapper = npaMapper;
    }

    @Transactional
    public NpaResponse create(NpaRequest request) {
        DictionaryValue npaType =
                getDictionaryValue(request.getNpaTypeId());

        DictionaryValue level =
                getDictionaryValue(request.getLevelId());

        DictionaryValue status =
                getDictionaryValue(request.getStatusId());

        Npa npa = npaMapper.toEntity(
                request,
                npaType,
                level,
                status
        );

        npa = npaRepository.save(npa);

        return npaMapper.toResponse(npa);
    }

    @Transactional(readOnly = true)
    public NpaResponse getById(UUID id) {
        Npa npa = npaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "НПА с id " + id + " не найден"
                        )
                );

        return npaMapper.toResponse(npa);
    }

    @Transactional(readOnly = true)
    public List<NpaResponse> getAll() {
        return npaRepository.findAll()
                .stream()
                .map(npaMapper::toResponse)
                .toList();
    }

    @Transactional
    public NpaResponse update(
            UUID id,
            NpaRequest request
    ) {
        Npa npa = npaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "НПА с id " + id + " не найден"
                        )
                );

        DictionaryValue npaType =
                getDictionaryValue(request.getNpaTypeId());

        DictionaryValue level =
                getDictionaryValue(request.getLevelId());

        DictionaryValue status =
                getDictionaryValue(request.getStatusId());

        npaMapper.updateEntity(
                npa,
                request,
                npaType,
                level,
                status
        );

        npa = npaRepository.save(npa);

        return npaMapper.toResponse(npa);
    }

    @Transactional
    public void delete(UUID id) {
        Npa npa = npaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "НПА с id " + id + " не найден"
                        )
                );

        npaRepository.delete(npa);
    }

    private DictionaryValue getDictionaryValue(UUID id) {
        return dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Значение справочника с id " + id + " не найдено"
                        )
                );
    }
}