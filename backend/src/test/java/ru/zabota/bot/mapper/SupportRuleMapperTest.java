package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import ru.zabota.bot.dto.support.SupportRuleRequest;
import ru.zabota.bot.dto.support.SupportRuleResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.entity.SupportRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit-тест SupportRuleMapper.
 *
 * Проверяет преобразование Request → Entity,
 * Entity → Response, обновление существующей Entity
 * и обработку null.
 */
class SupportRuleMapperTest {

    private final SupportRuleMapper mapper = new SupportRuleMapper();

    @Test
    void shouldMapRequestToEntity() {
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleRequest request = createRequest();

        SupportMeasure support = new SupportMeasure();
        support.setSupportId(supportId);
        support.setName("Ежемесячная выплата");

        DictionaryValue operator = createDictionaryValue(
                operatorId,
                "EQ",
                "Равно"
        );

        SupportRule entity = mapper.toEntity(
                request,
                support,
                operator
        );

        assertEquals(support, entity.getSupport());
        assertEquals(request.getParameter(), entity.getParameter());
        assertEquals(operator, entity.getOperator());
        assertEquals(request.getValueType(), entity.getValueType());
        assertEquals(request.getValueFrom(), entity.getValueFrom());
        assertEquals(request.getValueTo(), entity.getValueTo());
        assertEquals(request.getValue(), entity.getValue());
        assertEquals(
                request.getConditionGroup(),
                entity.getConditionGroup()
        );
        assertEquals(
                request.isRequired(),
                entity.isRequired()
        );
        assertEquals(
                request.getAmountOverride(),
                entity.getAmountOverride()
        );
    }

    @Test
    void shouldMapEntityToResponse() {
        UUID ruleId = UUID.randomUUID();
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        LocalDateTime createdAt = LocalDateTime.of(
                2026,
                9,
                22,
                21,
                30
        );

        SupportMeasure support = new SupportMeasure();
        support.setSupportId(supportId);
        support.setName("Ежемесячная выплата");

        DictionaryValue operator = createDictionaryValue(
                operatorId,
                "GTE",
                "Больше или равно"
        );

        SupportRule entity = new SupportRule();
        entity.setRuleId(ruleId);
        entity.setSupport(support);
        entity.setParameter("income");
        entity.setOperator(operator);
        entity.setValueType("NUMBER");
        entity.setValueFrom("10000");
        entity.setValueTo("30000");
        entity.setValue("20000");
        entity.setConditionGroup(2);
        entity.setRequired(true);
        entity.setAmountOverride(new BigDecimal("15000.00"));
        entity.setCreatedAt(createdAt);

        SupportRuleResponse response = mapper.toResponse(entity);

        assertEquals(ruleId, response.getRuleId());

        assertEquals(supportId, response.getSupportId());
        assertEquals(
                "Ежемесячная выплата",
                response.getSupportName()
        );

        assertEquals("income", response.getParameter());

        assertEquals(operatorId, response.getOperatorId());
        assertEquals("GTE", response.getOperatorCode());
        assertEquals("Больше или равно", response.getOperatorName());

        assertEquals("NUMBER", response.getValueType());
        assertEquals("10000", response.getValueFrom());
        assertEquals("30000", response.getValueTo());
        assertEquals("20000", response.getValue());

        assertEquals(2, response.getConditionGroup());
        assertEquals(true, response.isRequired());
        assertEquals(
                new BigDecimal("15000.00"),
                response.getAmountOverride()
        );
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    void shouldUpdateExistingEntity() {
        SupportRule entity = new SupportRule();

        SupportRuleRequest request = createRequest();

        SupportMeasure support = new SupportMeasure();
        support.setSupportId(UUID.randomUUID());
        support.setName("Ежемесячная выплата");

        DictionaryValue operator = createDictionaryValue(
                UUID.randomUUID(),
                "EQ",
                "Равно"
        );

        mapper.updateEntity(
                entity,
                request,
                support,
                operator
        );

        assertEquals(support, entity.getSupport());
        assertEquals(request.getParameter(), entity.getParameter());
        assertEquals(operator, entity.getOperator());
        assertEquals(request.getValueType(), entity.getValueType());
        assertEquals(request.getValueFrom(), entity.getValueFrom());
        assertEquals(request.getValueTo(), entity.getValueTo());
        assertEquals(request.getValue(), entity.getValue());
        assertEquals(
                request.getConditionGroup(),
                entity.getConditionGroup()
        );
        assertEquals(
                request.isRequired(),
                entity.isRequired()
        );
        assertEquals(
                request.getAmountOverride(),
                entity.getAmountOverride()
        );
    }

    @Test
    void shouldMapEntityWithoutOptionalReferences() {
        SupportRule entity = new SupportRule();

        entity.setParameter("disability");
        entity.setValueType("BOOLEAN");
        entity.setConditionGroup(1);

        SupportRuleResponse response = mapper.toResponse(entity);

        assertEquals("disability", response.getParameter());
        assertEquals("BOOLEAN", response.getValueType());
        assertEquals(1, response.getConditionGroup());

        assertNull(response.getSupportId());
        assertNull(response.getSupportName());

        assertNull(response.getOperatorId());
        assertNull(response.getOperatorCode());
        assertNull(response.getOperatorName());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        SupportRule entity = mapper.toEntity(
                null,
                null,
                null
        );

        assertNull(entity);
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        SupportRuleResponse response = mapper.toResponse(null);

        assertNull(response);
    }

    private SupportRuleRequest createRequest() {
        SupportRuleRequest request = new SupportRuleRequest();

        request.setSupportId(UUID.randomUUID());
        request.setParameter("income");
        request.setOperatorId(UUID.randomUUID());
        request.setValueType("NUMBER");
        request.setValueFrom("10000");
        request.setValueTo("30000");
        request.setValue("20000");
        request.setConditionGroup(2);
        request.setRequired(true);
        request.setAmountOverride(
                new BigDecimal("15000.00")
        );

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