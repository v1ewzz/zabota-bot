package ru.zabota.bot.dto.support;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/*
 * DTO для создания и обновления правила подбора меры поддержки.
 *
 * Содержит параметр, оператор, тип значения и сами значения
 * условия, а также номер группы условия.
 */
public class SupportRuleRequest {

    @NotNull(message = "Мера поддержки обязательна")
    private UUID supportId;

    @NotBlank(message = "Параметр правила обязателен")
    @Size(max = 100, message = "Параметр не должен превышать 100 символов")
    private String parameter;

    @NotNull(message = "Оператор обязателен")
    private UUID operatorId;

    @NotBlank(message = "Тип значения обязателен")
    @Size(max = 20, message = "Тип значения не должен превышать 20 символов")
    private String valueType;

    @Size(max = 255, message = "Начальное значение не должно превышать 255 символов")
    private String valueFrom;

    @Size(max = 255, message = "Конечное значение не должно превышать 255 символов")
    private String valueTo;

    @Size(max = 255, message = "Значение не должно превышать 255 символов")
    private String value;

    @NotNull(message = "Группа условия обязательна")
    private Integer conditionGroup;

    private boolean required;

    private BigDecimal amountOverride;

    public SupportRuleRequest() {
    }

    public UUID getSupportId() {
        return supportId;
    }

    public void setSupportId(UUID supportId) {
        this.supportId = supportId;
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
}