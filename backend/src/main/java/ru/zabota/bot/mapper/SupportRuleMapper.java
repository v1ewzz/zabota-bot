package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.support.SupportRuleRequest;
import ru.zabota.bot.dto.support.SupportRuleResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.entity.SupportRule;

/*
 * Маппер сущности SupportRule.
 *
 * Отвечает за ручное преобразование:
 *
 * SupportRuleRequest → SupportRule
 * SupportRule → SupportRuleResponse
 *
 * Связанные SupportMeasure и DictionaryValue передаются
 * из сервисного слоя и не загружаются маппером самостоятельно.
 */
@Component
public class SupportRuleMapper {

    public SupportRule toEntity(
            SupportRuleRequest request,
            SupportMeasure support,
            DictionaryValue operator
    ) {
        if (request == null) {
            return null;
        }

        SupportRule supportRule = new SupportRule();

        updateEntity(
                supportRule,
                request,
                support,
                operator
        );

        return supportRule;
    }

    public void updateEntity(
            SupportRule supportRule,
            SupportRuleRequest request,
            SupportMeasure support,
            DictionaryValue operator
    ) {
        if (supportRule == null || request == null) {
            return;
        }

        supportRule.setSupport(support);
        supportRule.setParameter(request.getParameter());
        supportRule.setOperator(operator);
        supportRule.setValueType(request.getValueType());
        supportRule.setValueFrom(request.getValueFrom());
        supportRule.setValueTo(request.getValueTo());
        supportRule.setValue(request.getValue());
        supportRule.setConditionGroup(request.getConditionGroup());
        supportRule.setRequired(request.isRequired());
        supportRule.setAmountOverride(request.getAmountOverride());
    }

    public SupportRuleResponse toResponse(SupportRule supportRule) {
        if (supportRule == null) {
            return null;
        }

        SupportRuleResponse response = new SupportRuleResponse();

        response.setRuleId(supportRule.getRuleId());
        response.setParameter(supportRule.getParameter());
        response.setValueType(supportRule.getValueType());
        response.setValueFrom(supportRule.getValueFrom());
        response.setValueTo(supportRule.getValueTo());
        response.setValue(supportRule.getValue());
        response.setConditionGroup(supportRule.getConditionGroup());
        response.setRequired(supportRule.isRequired());
        response.setAmountOverride(supportRule.getAmountOverride());
        response.setCreatedAt(supportRule.getCreatedAt());

        SupportMeasure support = supportRule.getSupport();

        if (support != null) {
            response.setSupportId(support.getSupportId());
            response.setSupportName(support.getName());
        }

        DictionaryValue operator = supportRule.getOperator();

        if (operator != null) {
            response.setOperatorId(operator.getDictionaryValueId());
            response.setOperatorCode(operator.getCode());
            response.setOperatorName(operator.getLabel());
        }

        return response;
    }
}