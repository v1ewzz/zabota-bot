package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.support.SupportRuleRequest;
import ru.zabota.bot.dto.support.SupportRuleResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.entity.SupportRule;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.SupportRuleMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.SupportMeasureRepository;
import ru.zabota.bot.repository.SupportRuleRepository;

import java.util.List;
import java.util.UUID;

/*
 * Сервис для работы с правилами мер социальной поддержки.
 *
 * Отвечает за CRUD-операции с SupportRule,
 * проверку существования связанной меры поддержки
 * и оператора из справочника.
 *
 * Также предоставляет получение правил конкретной
 * меры поддержки.
 *
 * Сервис не содержит сам алгоритм сопоставления условий
 * с профилем пользователя. Этот алгоритм будет находиться
 * в SupportMatchingService.
 */
@Service
public class SupportRuleService {

    private final SupportRuleRepository supportRuleRepository;
    private final SupportMeasureRepository supportMeasureRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final SupportRuleMapper supportRuleMapper;

    public SupportRuleService(
            SupportRuleRepository supportRuleRepository,
            SupportMeasureRepository supportMeasureRepository,
            DictionaryValueRepository dictionaryValueRepository,
            SupportRuleMapper supportRuleMapper
    ) {
        this.supportRuleRepository = supportRuleRepository;
        this.supportMeasureRepository = supportMeasureRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.supportRuleMapper = supportRuleMapper;
    }

    @Transactional
    public SupportRuleResponse create(
            SupportRuleRequest request
    ) {
        SupportMeasure supportMeasure =
                getSupportMeasure(request.getSupportId());

        DictionaryValue operator =
                getDictionaryValue(request.getOperatorId());

        SupportRule supportRule =
                supportRuleMapper.toEntity(
                        request,
                        supportMeasure,
                        operator
                );

        supportRule =
                supportRuleRepository.save(supportRule);

        return supportRuleMapper.toResponse(supportRule);
    }

    @Transactional(readOnly = true)
    public SupportRuleResponse getById(UUID id) {
        SupportRule supportRule =
                supportRuleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Правило с id "
                                                + id
                                                + " не найдено"
                                )
                        );

        return supportRuleMapper.toResponse(supportRule);
    }

    @Transactional(readOnly = true)
    public List<SupportRuleResponse> getAll() {
        return supportRuleRepository.findAll()
                .stream()
                .map(supportRuleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SupportRuleResponse> getBySupportId(
            UUID supportId
    ) {
        getSupportMeasure(supportId);

        return supportRuleRepository
                .findAllBySupport_SupportId(supportId)
                .stream()
                .map(supportRuleMapper::toResponse)
                .toList();
    }

    @Transactional
    public SupportRuleResponse update(
            UUID id,
            SupportRuleRequest request
    ) {
        SupportRule supportRule =
                supportRuleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Правило с id "
                                                + id
                                                + " не найдено"
                                )
                        );

        SupportMeasure supportMeasure =
                getSupportMeasure(request.getSupportId());

        DictionaryValue operator =
                getDictionaryValue(request.getOperatorId());

        supportRuleMapper.updateEntity(
                supportRule,
                request,
                supportMeasure,
                operator
        );

        supportRule =
                supportRuleRepository.save(supportRule);

        return supportRuleMapper.toResponse(supportRule);
    }

    @Transactional
    public void delete(UUID id) {
        SupportRule supportRule =
                supportRuleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Правило с id "
                                                + id
                                                + " не найдено"
                                )
                        );

        supportRuleRepository.delete(supportRule);
    }

    private SupportMeasure getSupportMeasure(UUID id) {
        return supportMeasureRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Мера поддержки с id "
                                        + id
                                        + " не найдена"
                        )
                );
    }

    private DictionaryValue getDictionaryValue(UUID id) {
        return dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Оператор с id "
                                        + id
                                        + " не найден"
                        )
                );
    }
}