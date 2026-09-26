package ru.zabota.bot.dto.support;

import java.math.BigDecimal;
import java.util.UUID;

/*
 * Сокращённый DTO меры социальной поддержки.
 *
 * Используется при выдаче результатов подбора пользователю.
 * Не содержит технические данные правила, НПА и внутренние связи,
 * которые не нужны в основном списке найденных мер.
 */
public class SupportMeasureShortResponse {

    private UUID supportId;
    private String name;
    private String description;
    private BigDecimal amount;
    private String frequency;
    private boolean applicationRequired;
    private String applicationChannel;
    private String actionUrl;

    public SupportMeasureShortResponse() {
    }

    public UUID getSupportId() {
        return supportId;
    }

    public void setSupportId(UUID supportId) {
        this.supportId = supportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isApplicationRequired() {
        return applicationRequired;
    }

    public void setApplicationRequired(boolean applicationRequired) {
        this.applicationRequired = applicationRequired;
    }

    public String getApplicationChannel() {
        return applicationChannel;
    }

    public void setApplicationChannel(String applicationChannel) {
        this.applicationChannel = applicationChannel;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}