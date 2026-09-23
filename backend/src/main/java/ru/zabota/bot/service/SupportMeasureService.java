package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.support.SupportMeasureRequest;
import ru.zabota.bot.dto.support.SupportMeasureResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.SupportMeasureMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.SupportMeasureRepository;

import java.util.List;
import java.util.UUID;

/*
 * Сервис для работы с мерами социальной поддержки.
 *
 * Отвечает за CRUD-операции с SupportMeasure.
 *
 * При создании и обновлении разрешает идентификаторы связанных
 * DictionaryValue в реальные сущности:
 *
 * - supportType;
 * - level;
 * - recipientType;
 * - applicationChannel;
 * - frequency;
 * - verificationStatus.
 *
 * applicationChannel и frequency являются необязательными
 * и могут отсутствовать.
 *
 * Связи SupportMeasure с НПА, муниципалитетами и правилами
 * на этом этапе отдельно не обрабатываются.
 */
@Service
public class SupportMeasureService {

    private final SupportMeasureRepository supportMeasureRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final SupportMeasureMapper supportMeasureMapper;

    public SupportMeasureService(
            SupportMeasureRepository supportMeasureRepository,
            DictionaryValueRepository dictionaryValueRepository,
            SupportMeasureMapper supportMeasureMapper
    ) {
        this.supportMeasureRepository = supportMeasureRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.supportMeasureMapper = supportMeasureMapper;
    }

    @Transactional
    public SupportMeasureResponse create(
            SupportMeasureRequest request
    ) {
        DictionaryValue supportType =
                getDictionaryValue(request.getSupportTypeId());

        DictionaryValue level =
                getDictionaryValue(request.getLevelId());

        DictionaryValue recipientType =
                getDictionaryValue(request.getRecipientTypeId());

        DictionaryValue applicationChannel =
                getOptionalDictionaryValue(
                        request.getApplicationChannelId()
                );

        DictionaryValue frequency =
                getOptionalDictionaryValue(
                        request.getFrequencyId()
                );

        DictionaryValue verificationStatus =
                getDictionaryValue(
                        request.getVerificationStatusId()
                );

        SupportMeasure supportMeasure =
                supportMeasureMapper.toEntity(
                        request,
                        supportType,
                        level,
                        recipientType,
                        applicationChannel,
                        frequency,
                        verificationStatus
                );

        supportMeasure =
                supportMeasureRepository.save(supportMeasure);

        return supportMeasureMapper.toResponse(supportMeasure);
    }

    @Transactional(readOnly = true)
    public SupportMeasureResponse getById(UUID id) {
        SupportMeasure supportMeasure =
                supportMeasureRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Мера поддержки с id "
                                                + id
                                                + " не найдена"
                                )
                        );

        return supportMeasureMapper.toResponse(supportMeasure);
    }

    @Transactional(readOnly = true)
    public List<SupportMeasureResponse> getAll() {
        return supportMeasureRepository.findAll()
                .stream()
                .map(supportMeasureMapper::toResponse)
                .toList();
    }

    @Transactional
    public SupportMeasureResponse update(
            UUID id,
            SupportMeasureRequest request
    ) {
        SupportMeasure supportMeasure =
                supportMeasureRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Мера поддержки с id "
                                                + id
                                                + " не найдена"
                                )
                        );

        DictionaryValue supportType =
                getDictionaryValue(request.getSupportTypeId());

        DictionaryValue level =
                getDictionaryValue(request.getLevelId());

        DictionaryValue recipientType =
                getDictionaryValue(request.getRecipientTypeId());

        DictionaryValue applicationChannel =
                getOptionalDictionaryValue(
                        request.getApplicationChannelId()
                );

        DictionaryValue frequency =
                getOptionalDictionaryValue(
                        request.getFrequencyId()
                );

        DictionaryValue verificationStatus =
                getDictionaryValue(
                        request.getVerificationStatusId()
                );

        supportMeasureMapper.updateEntity(
                supportMeasure,
                request,
                supportType,
                level,
                recipientType,
                applicationChannel,
                frequency,
                verificationStatus
        );

        supportMeasure =
                supportMeasureRepository.save(supportMeasure);

        return supportMeasureMapper.toResponse(supportMeasure);
    }

    @Transactional
    public void delete(UUID id) {
        SupportMeasure supportMeasure =
                supportMeasureRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Мера поддержки с id "
                                                + id
                                                + " не найдена"
                                )
                        );

        supportMeasureRepository.delete(supportMeasure);
    }

    private DictionaryValue getDictionaryValue(UUID id) {
        return dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Значение справочника с id "
                                        + id
                                        + " не найдено"
                        )
                );
    }

    private DictionaryValue getOptionalDictionaryValue(UUID id) {
        if (id == null) {
            return null;
        }

        return getDictionaryValue(id);
    }
}