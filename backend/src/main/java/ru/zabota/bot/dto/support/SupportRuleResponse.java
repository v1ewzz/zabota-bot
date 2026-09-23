package ru.zabota.bot.dto.support;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * DTO для возврата информации о правиле подбора.
 *
 * Содержит данные правила и основные сведения о связанных
 * мере поддержки и операторе.
 */
public class SupportRuleResponse {

    private UUID ruleId;

    private UUID supportId;
    private String supportName;

    private String parameter;

    private UUID operatorId;
    private String operatorCode;
    private String operatorName;

    private String valueType;
    private String valueFrom;
    private String valueTo;
    private String value;

    private Integer conditionGroup;

    private boolean required;

    private BigDecimal amountOverride;

    private LocalDateTime createdAt;

    public SupportRuleResponse() {
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public UUID getSupportId() {
        return supportId;
    }

    public void setSupportId(UUID supportId) {
        this.supportId = supportId;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public UUID getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(UUID operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(String valueFrom) {
        this.valueFrom = valueFrom;
    }

    public String getValueTo() {
        return valueTo;
    }

    public void setValueTo(String valueTo) {
        this.valueTo = valueTo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getConditionGroup() {
        return conditionGroup;
    }

    public void setConditionGroup(Integer conditionGroup) {
        this.conditionGroup = conditionGroup;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public BigDecimal getAmountOverride() {
        return amountOverride;
    }

    public void setAmountOverride(BigDecimal amountOverride) {
        this.amountOverride = amountOverride;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}