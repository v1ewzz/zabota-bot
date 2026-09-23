package ru.zabota.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.support.SupportMeasureRequest;
import ru.zabota.bot.dto.support.SupportMeasureResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.SupportMeasureMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.SupportMeasureRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit-тест SupportMeasureService.
 *
 * Проверяет:
 *
 * - создание меры поддержки;
 * - получение по id;
 * - получение списка;
 * - обновление;
 * - удаление;
 * - обработку отсутствующих обязательных справочников;
 * - работу с необязательными applicationChannel и frequency.
 *
 * Spring-контекст и база данных не используются.
 * Repository и Mapper заменяются Mockito-моками.
 */
@ExtendWith(MockitoExtension.class)
class SupportMeasureServiceTest {

    @Mock
    private SupportMeasureRepository supportMeasureRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private SupportMeasureMapper supportMeasureMapper;

    @InjectMocks
    private SupportMeasureService supportMeasureService;

    @Test
    void shouldCreateSupportMeasure() {
        UUID supportTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID recipientTypeId = UUID.randomUUID();
        UUID applicationChannelId = UUID.randomUUID();
        UUID frequencyId = UUID.randomUUID();
        UUID verificationStatusId = UUID.randomUUID();

        SupportMeasureRequest request =
                createRequest(
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        applicationChannelId,
                        frequencyId,
                        verificationStatusId
                );

        DictionaryValue supportType =
                createDictionaryValue(
                        supportTypeId,
                        "PAYMENT",
                        "Выплата"
                );

        DictionaryValue level =
                createDictionaryValue(
                        levelId,
                        "REGIONAL",
                        "Региональный"
                );

        DictionaryValue recipientType =
                createDictionaryValue(
                        recipientTypeId,
                        "FAMILY",
                        "Семья"
                );

        DictionaryValue applicationChannel =
                createDictionaryValue(
                        applicationChannelId,
                        "MFC",
                        "МФЦ"
                );

        DictionaryValue frequency =
                createDictionaryValue(
                        frequencyId,
                        "MONTHLY",
                        "Ежемесячно"
                );

        DictionaryValue verificationStatus =
                createDictionaryValue(
                        verificationStatusId,
                        "VERIFIED",
                        "Проверено"
                );

        SupportMeasure supportMeasure =
                new SupportMeasure();

        SupportMeasureResponse expected =
                createResponse(UUID.randomUUID());

        when(dictionaryValueRepository.findById(supportTypeId))
                .thenReturn(Optional.of(supportType));

        when(dictionaryValueRepository.findById(levelId))
                .thenReturn(Optional.of(level));

        when(dictionaryValueRepository.findById(recipientTypeId))
                .thenReturn(Optional.of(recipientType));

        when(dictionaryValueRepository.findById(applicationChannelId))
                .thenReturn(Optional.of(applicationChannel));

        when(dictionaryValueRepository.findById(frequencyId))
                .thenReturn(Optional.of(frequency));

        when(dictionaryValueRepository.findById(verificationStatusId))
                .thenReturn(Optional.of(verificationStatus));

        when(supportMeasureMapper.toEntity(
                request,
                supportType,
                level,
                recipientType,
                applicationChannel,
                frequency,
                verificationStatus
        )).thenReturn(supportMeasure);

        when(supportMeasureRepository.save(supportMeasure))
                .thenReturn(supportMeasure);

        when(supportMeasureMapper.toResponse(supportMeasure))
                .thenReturn(expected);

        SupportMeasureResponse result =
                supportMeasureService.create(request);

        assertEquals(expected, result);

        verify(supportMeasureRepository)
                .save(supportMeasure);

        verify(supportMeasureMapper)
                .toResponse(supportMeasure);
    }

    @Test
    void shouldCreateSupportMeasureWithoutOptionalDictionaryValues() {
        UUID supportTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID recipientTypeId = UUID.randomUUID();
        UUID verificationStatusId = UUID.randomUUID();

        SupportMeasureRequest request =
                createRequest(
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        null,
                        null,
                        verificationStatusId
                );

        DictionaryValue supportType =
                createDictionaryValue(
                        supportTypeId,
                        "PAYMENT",
                        "Выплата"
                );

        DictionaryValue level =
                createDictionaryValue(
                        levelId,
                        "REGIONAL",
                        "Региональный"
                );

        DictionaryValue recipientType =
                createDictionaryValue(
                        recipientTypeId,
                        "FAMILY",
                        "Семья"
                );

        DictionaryValue verificationStatus =
                createDictionaryValue(
                        verificationStatusId,
                        "VERIFIED",
                        "Проверено"
                );

        SupportMeasure supportMeasure =
                new SupportMeasure();

        SupportMeasureResponse expected =
                createResponse(UUID.randomUUID());

        when(dictionaryValueRepository.findById(supportTypeId))
                .thenReturn(Optional.of(supportType));

        when(dictionaryValueRepository.findById(levelId))
                .thenReturn(Optional.of(level));

        when(dictionaryValueRepository.findById(recipientTypeId))
                .thenReturn(Optional.of(recipientType));

        when(dictionaryValueRepository.findById(verificationStatusId))
                .thenReturn(Optional.of(verificationStatus));

        when(supportMeasureMapper.toEntity(
                request,
                supportType,
                level,
                recipientType,
                null,
                null,
                verificationStatus
        )).thenReturn(supportMeasure);

        when(supportMeasureRepository.save(supportMeasure))
                .thenReturn(supportMeasure);

        when(supportMeasureMapper.toResponse(supportMeasure))
                .thenReturn(expected);

        SupportMeasureResponse result =
                supportMeasureService.create(request);

        assertEquals(expected, result);

        verify(dictionaryValueRepository, never())
                .findById(null);

        verify(supportMeasureRepository)
                .save(supportMeasure);
    }

    @Test
    void shouldThrowExceptionWhenSupportTypeNotFoundOnCreate() {
        UUID supportTypeId = UUID.randomUUID();

        SupportMeasureRequest request =
                createRequestWithIds(
                        supportTypeId,
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null,
                        null,
                        UUID.randomUUID()
                );

        when(dictionaryValueRepository.findById(supportTypeId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportMeasureService.create(request)
        );

        verify(supportMeasureRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldGetSupportMeasureById() {
        UUID id = UUID.randomUUID();

        SupportMeasure supportMeasure =
                new SupportMeasure();

        SupportMeasureResponse expected =
                createResponse(id);

        when(supportMeasureRepository.findById(id))
                .thenReturn(Optional.of(supportMeasure));

        when(supportMeasureMapper.toResponse(supportMeasure))
                .thenReturn(expected);

        SupportMeasureResponse result =
                supportMeasureService.getById(id);

        assertEquals(expected, result);

        verify(supportMeasureRepository)
                .findById(id);

        verify(supportMeasureMapper)
                .toResponse(supportMeasure);
    }

    @Test
    void shouldThrowExceptionWhenSupportMeasureNotFound() {
        UUID id = UUID.randomUUID();

        when(supportMeasureRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportMeasureService.getById(id)
        );

        verify(supportMeasureRepository)
                .findById(id);

        verify(supportMeasureMapper, never())
                .toResponse(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    @Test
    void shouldGetAllSupportMeasures() {
        SupportMeasure first =
                new SupportMeasure();

        SupportMeasure second =
                new SupportMeasure();

        SupportMeasureResponse firstResponse =
                createResponse(UUID.randomUUID());

        SupportMeasureResponse secondResponse =
                createResponse(UUID.randomUUID());

        when(supportMeasureRepository.findAll())
                .thenReturn(List.of(first, second));

        when(supportMeasureMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(supportMeasureMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<SupportMeasureResponse> result =
                supportMeasureService.getAll();

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(supportMeasureRepository)
                .findAll();

        verify(supportMeasureMapper)
                .toResponse(first);

        verify(supportMeasureMapper)
                .toResponse(second);
    }

    @Test
    void shouldUpdateSupportMeasure() {
        UUID supportId = UUID.randomUUID();

        UUID supportTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID recipientTypeId = UUID.randomUUID();
        UUID applicationChannelId = UUID.randomUUID();
        UUID frequencyId = UUID.randomUUID();
        UUID verificationStatusId = UUID.randomUUID();

        SupportMeasureRequest request =
                createRequest(
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        applicationChannelId,
                        frequencyId,
                        verificationStatusId
                );

        SupportMeasure supportMeasure =
                new SupportMeasure();

        DictionaryValue supportType =
                createDictionaryValue(
                        supportTypeId,
                        "PAYMENT",
                        "Выплата"
                );

        DictionaryValue level =
                createDictionaryValue(
                        levelId,
                        "REGIONAL",
                        "Региональный"
                );

        DictionaryValue recipientType =
                createDictionaryValue(
                        recipientTypeId,
                        "FAMILY",
                        "Семья"
                );

        DictionaryValue applicationChannel =
                createDictionaryValue(
                        applicationChannelId,
                        "MFC",
                        "МФЦ"
                );

        DictionaryValue frequency =
                createDictionaryValue(
                        frequencyId,
                        "MONTHLY",
                        "Ежемесячно"
                );

        DictionaryValue verificationStatus =
                createDictionaryValue(
                        verificationStatusId,
                        "VERIFIED",
                        "Проверено"
                );

        SupportMeasureResponse expected =
                createResponse(supportId);

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.of(supportMeasure));

        when(dictionaryValueRepository.findById(supportTypeId))
                .thenReturn(Optional.of(supportType));

        when(dictionaryValueRepository.findById(levelId))
                .thenReturn(Optional.of(level));

        when(dictionaryValueRepository.findById(recipientTypeId))
                .thenReturn(Optional.of(recipientType));

        when(dictionaryValueRepository.findById(applicationChannelId))
                .thenReturn(Optional.of(applicationChannel));

        when(dictionaryValueRepository.findById(frequencyId))
                .thenReturn(Optional.of(frequency));

        when(dictionaryValueRepository.findById(verificationStatusId))
                .thenReturn(Optional.of(verificationStatus));

        when(supportMeasureRepository.save(supportMeasure))
                .thenReturn(supportMeasure);

        when(supportMeasureMapper.toResponse(supportMeasure))
                .thenReturn(expected);

        SupportMeasureResponse result =
                supportMeasureService.update(
                        supportId,
                        request
                );

        assertEquals(expected, result);

        verify(supportMeasureMapper)
                .updateEntity(
                        supportMeasure,
                        request,
                        supportType,
                        level,
                        recipientType,
                        applicationChannel,
                        frequency,
                        verificationStatus
                );

        verify(supportMeasureRepository)
                .save(supportMeasure);

        verify(supportMeasureMapper)
                .toResponse(supportMeasure);
    }

    @Test
    void shouldThrowExceptionWhenSupportMeasureNotFoundOnUpdate() {
        UUID supportId = UUID.randomUUID();

        SupportMeasureRequest request =
                createRequestWithIds(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        null,
                        null,
                        UUID.randomUUID()
                );

        when(supportMeasureRepository.findById(supportId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportMeasureService.update(
                        supportId,
                        request
                )
        );

        verify(supportMeasureMapper, never())
                .updateEntity(
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.any()
                );

        verify(supportMeasureRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldDeleteSupportMeasure() {
        UUID id = UUID.randomUUID();

        SupportMeasure supportMeasure =
                new SupportMeasure();

        when(supportMeasureRepository.findById(id))
                .thenReturn(Optional.of(supportMeasure));

        supportMeasureService.delete(id);

        verify(supportMeasureRepository)
                .findById(id);

        verify(supportMeasureRepository)
                .delete(supportMeasure);
    }

    @Test
    void shouldThrowExceptionWhenSupportMeasureNotFoundOnDelete() {
        UUID id = UUID.randomUUID();

        when(supportMeasureRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> supportMeasureService.delete(id)
        );

        verify(supportMeasureRepository)
                .findById(id);

        verify(supportMeasureRepository, never())
                .delete(
                        org.mockito.ArgumentMatchers.any()
                );
    }

    private SupportMeasureRequest createRequest(
            UUID supportTypeId,
            UUID levelId,
            UUID recipientTypeId,
            UUID applicationChannelId,
            UUID frequencyId,
            UUID verificationStatusId
    ) {
        return createRequestWithIds(
                supportTypeId,
                levelId,
                recipientTypeId,
                applicationChannelId,
                frequencyId,
                verificationStatusId
        );
    }

    private SupportMeasureRequest createRequestWithIds(
            UUID supportTypeId,
            UUID levelId,
            UUID recipientTypeId,
            UUID applicationChannelId,
            UUID frequencyId,
            UUID verificationStatusId
    ) {
        SupportMeasureRequest request =
                new SupportMeasureRequest();

        request.setName("Ежемесячная выплата");
        request.setDescription(
                "Мера социальной поддержки семьи"
        );
        request.setSupportTypeId(supportTypeId);
        request.setLevelId(levelId);
        request.setRecipientTypeId(recipientTypeId);
        request.setApplicationRequired(true);
        request.setApplicationChannelId(applicationChannelId);
        request.setAmount(new BigDecimal("20000.00"));
        request.setFrequencyId(frequencyId);
        request.setDocuments(
                "Паспорт, документы о составе семьи"
        );
        request.setValidFrom(null);
        request.setValidTo(null);
        request.setVerificationStatusId(
                verificationStatusId
        );
        request.setActionUrl(
                "https://example.com/apply"
        );

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

    private SupportMeasureResponse createResponse(UUID id) {
        SupportMeasureResponse response =
                new SupportMeasureResponse();

        response.setSupportId(id);
        response.setName("Ежемесячная выплата");
        response.setDescription(
                "Мера социальной поддержки семьи"
        );

        return response;
    }
}