package ru.zabota.bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность правила подбора меры социальной поддержки.
 *
 * Соответствует таблице support_rule.
 * Хранит одно условие, по которому алгоритм определяет,
 * подходит ли конкретная мера поддержки пользователю.
 *
 * Несколько правил одной меры объединяются по condition_group:
 * правила одной группы рассматриваются как AND,
 * разные группы — как OR.
 */
@Entity
@Table(name = "support_rule")
public class SupportRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rule_id", nullable = false, updatable = false)
    private UUID ruleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "support_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_rule_support")
    )
    private SupportMeasure support;

    @Column(name = "parameter", nullable = false, length = 100)
    private String parameter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "operator_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_rule_operator")
    )
    private DictionaryValue operator;

    @Column(name = "value_type", nullable = false, length = 20)
    private String valueType;

    @Column(name = "value_from", length = 255)
    private String valueFrom;

    @Column(name = "value_to", length = 255)
    private String valueTo;

    @Column(name = "value", length = 255)
    private String value;

    @Column(name = "condition_group", nullable = false)
    private Integer conditionGroup;

    @Column(name = "is_required", nullable = false)
    private boolean required = false;

    @Column(name = "amount_override", precision = 12, scale = 2)
    private BigDecimal amountOverride;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public SupportRule() {
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public SupportMeasure getSupport() {
        return support;
    }

    public void setSupport(SupportMeasure support) {
        this.support = support;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public DictionaryValue getOperator() {
        return operator;
    }

    public void setOperator(DictionaryValue operator) {
        this.operator = operator;
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