package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import ru.zabota.bot.dto.support.SupportMeasureRequest;
import ru.zabota.bot.dto.support.SupportMeasureResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.SupportMeasure;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit-тест SupportMeasureMapper.
 *
 * Проверяет преобразование Request → Entity,
 * Entity → Response, обновление существующей Entity
 * и обработку null.
 */
class SupportMeasureMapperTest {

    private final SupportMeasureMapper mapper = new SupportMeasureMapper();

    @Test
    void shouldMapRequestToEntity() {
        UUID supportTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID recipientTypeId = UUID.randomUUID();
        UUID applicationChannelId = UUID.randomUUID();
        UUID frequencyId = UUID.randomUUID();
        UUID verificationStatusId = UUID.randomUUID();

        SupportMeasureRequest request = createRequest();

        DictionaryValue supportType = createDictionaryValue(
                supportTypeId,
                "PAYMENT",
                "Выплата"
        );

        DictionaryValue level = createDictionaryValue(
                levelId,
                "FEDERAL",
                "Федеральный"
        );

        DictionaryValue recipientType = createDictionaryValue(
                recipientTypeId,
                "FAMILY",
                "Семья"
        );

        DictionaryValue applicationChannel = createDictionaryValue(
                applicationChannelId,
                "MFC",
                "МФЦ"
        );

        DictionaryValue frequency = createDictionaryValue(
                frequencyId,
                "MONTHLY",
                "Ежемесячно"
        );

        DictionaryValue verificationStatus = createDictionaryValue(
                verificationStatusId,
                "VERIFIED",
                "Проверено"
        );

        SupportMeasure entity = mapper.toEntity(
                request,
                supportType,
                level,
                recipientType,
                applicationChannel,
                frequency,
                verificationStatus
        );

        assertEquals(request.getName(), entity.getName());
        assertEquals(request.getDescription(), entity.getDescription());

        assertEquals(supportType, entity.getSupportType());
        assertEquals(level, entity.getLevel());
        assertEquals(recipientType, entity.getRecipientType());

        assertEquals(
                request.getApplicationRequired(),
                entity.isApplicationRequired()
        );

        assertEquals(
                applicationChannel,
                entity.getApplicationChannel()
        );

        assertEquals(request.getAmount(), entity.getAmount());
        assertEquals(frequency, entity.getFrequency());
        assertEquals(request.getDocuments(), entity.getDocuments());
        assertEquals(request.getValidFrom(), entity.getValidFrom());
        assertEquals(request.getValidTo(), entity.getValidTo());
        assertEquals(
                verificationStatus,
                entity.getVerificationStatus()
        );
        assertEquals(request.getActionUrl(), entity.getActionUrl());
    }

    @Test
    void shouldMapEntityToResponse() {
        UUID supportId = UUID.randomUUID();

        SupportMeasure entity = new SupportMeasure();
        entity.setSupportId(supportId);
        entity.setName("Ежемесячная выплата");
        entity.setDescription("Мера социальной поддержки");
        entity.setApplicationRequired(true);
        entity.setAmount(new BigDecimal("15000.00"));
        entity.setDocuments("Паспорт, заявление");
        entity.setValidFrom(LocalDate.of(2026, 1, 1));
        entity.setValidTo(LocalDate.of(2026, 12, 31));
        entity.setActionUrl("https://example.ru/apply");

        DictionaryValue supportType = createDictionaryValue(
                UUID.randomUUID(),
                "PAYMENT",
                "Выплата"
        );

        DictionaryValue level = createDictionaryValue(
                UUID.randomUUID(),
                "FEDERAL",
                "Федеральный"
        );

        DictionaryValue recipientType = createDictionaryValue(
                UUID.randomUUID(),
                "FAMILY",
                "Семья"
        );

        DictionaryValue applicationChannel = createDictionaryValue(
                UUID.randomUUID(),
                "MFC",
                "МФЦ"
        );

        DictionaryValue frequency = createDictionaryValue(
                UUID.randomUUID(),
                "MONTHLY",
                "Ежемесячно"
        );

        DictionaryValue verificationStatus = createDictionaryValue(
                UUID.randomUUID(),
                "VERIFIED",
                "Проверено"
        );

        entity.setSupportType(supportType);
        entity.setLevel(level);
        entity.setRecipientType(recipientType);
        entity.setApplicationChannel(applicationChannel);
        entity.setFrequency(frequency);
        entity.setVerificationStatus(verificationStatus);

        LocalDateTime createdAt = LocalDateTime.of(
                2026, 1, 10, 12, 0
        );

        LocalDateTime updatedAt = LocalDateTime.of(
                2026, 2, 10, 12, 0
        );

        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        SupportMeasureResponse response = mapper.toResponse(entity);

        assertEquals(supportId, response.getSupportId());
        assertEquals("Ежемесячная выплата", response.getName());
        assertEquals(
                "Мера социальной поддержки",
                response.getDescription()
        );

        assertEquals(
                supportType.getDictionaryValueId(),
                response.getSupportTypeId()
        );
        assertEquals("PAYMENT", response.getSupportTypeCode());
        assertEquals("Выплата", response.getSupportTypeName());

        assertEquals(
                level.getDictionaryValueId(),
                response.getLevelId()
        );
        assertEquals("FEDERAL", response.getLevelCode());
        assertEquals("Федеральный", response.getLevelName());

        assertEquals(
                recipientType.getDictionaryValueId(),
                response.getRecipientTypeId()
        );
        assertEquals("FAMILY", response.getRecipientTypeCode());
        assertEquals("Семья", response.getRecipientTypeName());

        assertEquals(
                applicationChannel.getDictionaryValueId(),
                response.getApplicationChannelId()
        );
        assertEquals("MFC", response.getApplicationChannelCode());
        assertEquals("МФЦ", response.getApplicationChannelName());

        assertEquals(
                new BigDecimal("15000.00"),
                response.getAmount()
        );

        assertEquals(
                frequency.getDictionaryValueId(),
                response.getFrequencyId()
        );
        assertEquals("MONTHLY", response.getFrequencyCode());
        assertEquals("Ежемесячно", response.getFrequencyName());

        assertEquals(
                verificationStatus.getDictionaryValueId(),
                response.getVerificationStatusId()
        );
        assertEquals(
                "VERIFIED",
                response.getVerificationStatusCode()
        );
        assertEquals(
                "Проверено",
                response.getVerificationStatusName()
        );

        assertEquals(
                "Паспорт, заявление",
                response.getDocuments()
        );
        assertEquals(
                LocalDate.of(2026, 1, 1),
                response.getValidFrom()
        );
        assertEquals(
                LocalDate.of(2026, 12, 31),
                response.getValidTo()
        );
        assertEquals(
                "https://example.ru/apply",
                response.getActionUrl()
        );
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    void shouldUpdateExistingEntity() {
        SupportMeasure entity = new SupportMeasure();
        entity.setName("Старое название");

        SupportMeasureRequest request = createRequest();

        DictionaryValue supportType = createDictionaryValue(
                UUID.randomUUID(),
                "PAYMENT",
                "Выплата"
        );

        DictionaryValue level = createDictionaryValue(
                UUID.randomUUID(),
                "FEDERAL",
                "Федеральный"
        );

        DictionaryValue recipientType = createDictionaryValue(
                UUID.randomUUID(),
                "FAMILY",
                "Семья"
        );

        DictionaryValue applicationChannel = createDictionaryValue(
                UUID.randomUUID(),
                "MFC",
                "МФЦ"
        );

        DictionaryValue frequency = createDictionaryValue(
                UUID.randomUUID(),
                "MONTHLY",
                "Ежемесячно"
        );

        DictionaryValue verificationStatus = createDictionaryValue(
                UUID.randomUUID(),
                "VERIFIED",
                "Проверено"
        );

        mapper.updateEntity(
                entity,
                request,
                supportType,
                level,
                recipientType,
                applicationChannel,
                frequency,
                verificationStatus
        );

        assertEquals(request.getName(), entity.getName());
        assertEquals(request.getDescription(), entity.getDescription());
        assertEquals(supportType, entity.getSupportType());
        assertEquals(level, entity.getLevel());
        assertEquals(recipientType, entity.getRecipientType());
        assertEquals(applicationChannel, entity.getApplicationChannel());
        assertEquals(frequency, entity.getFrequency());
        assertEquals(
                verificationStatus,
                entity.getVerificationStatus()
        );
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        SupportMeasure entity = mapper.toEntity(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertNull(entity);
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        SupportMeasureResponse response = mapper.toResponse(null);

        assertNull(response);
    }

    private SupportMeasureRequest createRequest() {
        SupportMeasureRequest request = new SupportMeasureRequest();

        request.setName("Ежемесячная выплата");
        request.setDescription("Мера социальной поддержки");
        request.setSupportTypeId(UUID.randomUUID());
        request.setLevelId(UUID.randomUUID());
        request.setRecipientTypeId(UUID.randomUUID());
        request.setApplicationRequired(true);
        request.setApplicationChannelId(UUID.randomUUID());
        request.setAmount(new BigDecimal("15000.00"));
        request.setFrequencyId(UUID.randomUUID());
        request.setDocuments("Паспорт, заявление");
        request.setValidFrom(LocalDate.of(2026, 1, 1));
        request.setValidTo(LocalDate.of(2026, 12, 31));
        request.setVerificationStatusId(UUID.randomUUID());
        request.setActionUrl("https://example.ru/apply");

        return request;
    }

    private DictionaryValue createDictionaryValue(
            UUID id,
            String code,
            String label
    ) {
        DictionaryValue value = new DictionaryValue();

        value.setDictionaryValueId(id);
        value.setCode(code);
        value.setLabel(label);
        value.setActive(true);

        return value;
    }
}