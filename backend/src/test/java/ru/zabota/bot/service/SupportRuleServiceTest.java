package ru.zabota.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit-тест SupportRuleService.
 *
 * Проверяет CRUD-операции, получение правил конкретной меры
 * поддержки и обработку отсутствующих связанных сущностей.
 *
 * Spring-контекст и база данных не используются.
 * Repository и Mapper заменяются Mockito-моками.
 */
@ExtendWith(MockitoExtension.class)
class SupportRuleServiceTest {

    @Mock
    private SupportRuleRepository supportRuleRepository;

    @Mock
    private SupportMeasureRepository supportMeasureRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private SupportRuleMapper supportRuleMapper;

    @InjectMocks
    private SupportRuleService supportRuleService;

    @Test
    void shouldCreateSupportRule() {
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();
        UUID ruleId = UUID.randomUUID();

        SupportRuleRequest request =
                createRequest(supportId, operatorId);

        SupportMeasure supportMeasure =
                new SupportMeasure();

        DictionaryValue operator =
                createDictionaryValue(
                        operatorId,
                        "EQUALS",
                        "Равно"
                );

        SupportRule supportRule =
                new SupportRule();

        SupportRuleResponse expected =
                createResponse(ruleId);

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.of(supportMeasure));

        when(dictionaryValueRepository.findById(operatorId))
                .thenReturn(Optional.of(operator));

        when(supportRuleMapper.toEntity(
                request,
                supportMeasure,
                operator
        )).thenReturn(supportRule);

        when(supportRuleRepository.save(supportRule))
                .thenReturn(supportRule);

        when(supportRuleMapper.toResponse(supportRule))
                .thenReturn(expected);

        SupportRuleResponse result =
                supportRuleService.create(request);

        assertEquals(expected, result);

        verify(supportMeasureRepository)
                .findById(supportId);

        verify(dictionaryValueRepository)
                .findById(operatorId);

        verify(supportRuleMapper)
                .toEntity(
                        request,
                        supportMeasure,
                        operator
                );

        verify(supportRuleRepository)
                .save(supportRule);

        verify(supportRuleMapper)
                .toResponse(supportRule);
    }

    @Test
    void shouldThrowExceptionWhenSupportMeasureNotFoundOnCreate() {
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleRequest request =
                createRequest(
                        supportId,
                        operatorId
                );

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportRuleService.create(request)
        );

        verify(supportMeasureRepository)
                .findById(supportId);

        verify(dictionaryValueRepository, never())
                .findById(operatorId);

        verify(supportRuleRepository, never())
                .save(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    @Test
    void shouldThrowExceptionWhenOperatorNotFoundOnCreate() {
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleRequest request =
                createRequest(
                        supportId,
                        operatorId
                );

        SupportMeasure supportMeasure =
                new SupportMeasure();

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.of(supportMeasure));

        when(dictionaryValueRepository.findById(operatorId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportRuleService.create(request)
        );

        verify(supportMeasureRepository)
                .findById(supportId);

        verify(dictionaryValueRepository)
                .findById(operatorId);

        verify(supportRuleRepository, never())
                .save(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    @Test
    void shouldGetSupportRuleById() {
        UUID ruleId = UUID.randomUUID();

        SupportRule supportRule =
                new SupportRule();

        SupportRuleResponse expected =
                createResponse(ruleId);

        when(supportRuleRepository.findById(ruleId))
                .thenReturn(Optional.of(supportRule));

        when(supportRuleMapper.toResponse(supportRule))
                .thenReturn(expected);

        SupportRuleResponse result =
                supportRuleService.getById(ruleId);

        assertEquals(expected, result);

        verify(supportRuleRepository)
                .findById(ruleId);

        verify(supportRuleMapper)
                .toResponse(supportRule);
    }

    @Test
    void shouldThrowExceptionWhenSupportRuleNotFound() {
        UUID ruleId = UUID.randomUUID();

        when(supportRuleRepository.findById(ruleId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportRuleService.getById(ruleId)
        );

        verify(supportRuleRepository)
                .findById(ruleId);

        verify(supportRuleMapper, never())
                .toResponse(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    @Test
    void shouldGetAllSupportRules() {
        SupportRule first =
                new SupportRule();

        SupportRule second =
                new SupportRule();

        SupportRuleResponse firstResponse =
                createResponse(UUID.randomUUID());

        SupportRuleResponse secondResponse =
                createResponse(UUID.randomUUID());

        when(supportRuleRepository.findAll())
                .thenReturn(List.of(first, second));

        when(supportRuleMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(supportRuleMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<SupportRuleResponse> result =
                supportRuleService.getAll();

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(supportRuleRepository)
                .findAll();

        verify(supportRuleMapper)
                .toResponse(first);

        verify(supportRuleMapper)
                .toResponse(second);
    }

    @Test
    void shouldGetSupportRulesBySupportId() {
        UUID supportId = UUID.randomUUID();

        SupportMeasure supportMeasure =
                new SupportMeasure();

        SupportRule first =
                new SupportRule();

        SupportRule second =
                new SupportRule();

        SupportRuleResponse firstResponse =
                createResponse(UUID.randomUUID());

        SupportRuleResponse secondResponse =
                createResponse(UUID.randomUUID());

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.of(supportMeasure));

        when(supportRuleRepository
                .findAllBySupport_SupportId(supportId))
                .thenReturn(List.of(first, second));

        when(supportRuleMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(supportRuleMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<SupportRuleResponse> result =
                supportRuleService.getBySupportId(supportId);

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(supportMeasureRepository)
                .findById(supportId);

        verify(supportRuleRepository)
                .findAllBySupport_SupportId(supportId);
    }

    @Test
    void shouldThrowExceptionWhenSupportMeasureNotFoundOnGetBySupportId() {
        UUID supportId = UUID.randomUUID();

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportRuleService.getBySupportId(supportId)
        );

        verify(supportMeasureRepository)
                .findById(supportId);

        verify(supportRuleRepository, never())
                .findAllBySupport_SupportId(supportId);
    }

    @Test
    void shouldUpdateSupportRule() {
        UUID ruleId = UUID.randomUUID();
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleRequest request =
                createRequest(
                        supportId,
                        operatorId
                );

        SupportRule supportRule =
                new SupportRule();

        SupportMeasure supportMeasure =
                new SupportMeasure();

        DictionaryValue operator =
                createDictionaryValue(
                        operatorId,
                        "EQUALS",
                        "Равно"
                );

        SupportRuleResponse expected =
                createResponse(ruleId);

        when(supportRuleRepository.findById(ruleId))
                .thenReturn(Optional.of(supportRule));

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.of(supportMeasure));

        when(dictionaryValueRepository.findById(operatorId))
                .thenReturn(Optional.of(operator));

        when(supportRuleRepository.save(supportRule))
                .thenReturn(supportRule);

        when(supportRuleMapper.toResponse(supportRule))
                .thenReturn(expected);

        SupportRuleResponse result =
                supportRuleService.update(
                        ruleId,
                        request
                );

        assertEquals(expected, result);

        verify(supportRuleRepository)
                .findById(ruleId);

        verify(supportMeasureRepository)
                .findById(supportId);

        verify(dictionaryValueRepository)
                .findById(operatorId);

        verify(supportRuleMapper)
                .updateEntity(
                        supportRule,
                        request,
                        supportMeasure,
                        operator
                );

        verify(supportRuleRepository)
                .save(supportRule);

        verify(supportRuleMapper)
                .toResponse(supportRule);
    }

    @Test
    void shouldThrowExceptionWhenSupportRuleNotFoundOnUpdate() {
        UUID ruleId = UUID.randomUUID();
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleRequest request =
                createRequest(
                        supportId,
                        operatorId
                );

        when(supportRuleRepository.findById(ruleId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportRuleService.update(
                        ruleId,
                        request
                )
        );

        verify(supportRuleRepository)
                .findById(ruleId);

        verify(supportMeasureRepository, never())
                .findById(supportId);

        verify(dictionaryValueRepository, never())
                .findById(operatorId);

        verify(supportRuleRepository, never())
                .save(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    @Test
    void shouldDeleteSupportRule() {
        UUID ruleId = UUID.randomUUID();

        SupportRule supportRule =
                new SupportRule();

        when(supportRuleRepository.findById(ruleId))
                .thenReturn(Optional.of(supportRule));

        supportRuleService.delete(ruleId);

        verify(supportRuleRepository)
                .findById(ruleId);

        verify(supportRuleRepository)
                .delete(supportRule);
    }

    @Test
    void shouldThrowExceptionWhenSupportRuleNotFoundOnDelete() {
        UUID ruleId = UUID.randomUUID();

        when(supportRuleRepository.findById(ruleId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportRuleService.delete(ruleId)
        );

        verify(supportRuleRepository)
                .findById(ruleId);

        verify(supportRuleRepository, never())
                .delete(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    private SupportRuleRequest createRequest(
            UUID supportId,
            UUID operatorId
    ) {
        SupportRuleRequest request =
                new SupportRuleRequest();

        request.setSupportId(supportId);
        request.setParameter("military_status");
        request.setOperatorId(operatorId);
        request.setValueType("REFERENCE");
        request.setValueFrom(null);
        request.setValueTo(null);
        request.setValue("MOBILIZED");
        request.setConditionGroup(1);
        request.setRequired(true);
        request.setAmountOverride(null);

        return request;
    }

    private DictionaryValue createDictionaryValue(
            UUID id,
            String code,
            String label
    ) {
        DictionaryValue value =
                new DictionaryValue();

        value.setDictionaryValueId(id);
        value.setCode(code);
        value.setLabel(label);

        return value;
    }

    private SupportRuleResponse createResponse(UUID id) {
        SupportRuleResponse response =
                new SupportRuleResponse();

        response.setRuleId(id);
        response.setParameter("military_status");
        response.setValueType("REFERENCE");
        response.setValue("MOBILIZED");

        return response;
    }
}