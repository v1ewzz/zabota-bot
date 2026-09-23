package ru.zabota.bot.dto.support;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/*
 * DTO для создания и обновления меры социальной поддержки.
 *
 * Содержит данные, необходимые для управления мерой поддержки
 * через API. Идентификатор, даты создания и обновления
 * сервером не передаются.
 *
 * Связанные справочные сущности передаются через UUID.
 */
public class SupportMeasureRequest {

    @NotBlank(message = "Название меры поддержки обязательно")
    @Size(
            max = 500,
            message = "Название меры поддержки не должно превышать 500 символов"
    )
    private String name;

    @NotBlank(message = "Описание меры поддержки обязательно")
    private String description;

    @NotNull(message = "Тип меры поддержки обязателен")
    private UUID supportTypeId;

    @NotNull(message = "Уровень предоставления обязателен")
    private UUID levelId;

    @NotNull(message = "Тип получателя обязателен")
    private UUID recipientTypeId;

    @NotNull(message = "Необходимо указать, требуется ли заявление")
    private Boolean applicationRequired;

    private UUID applicationChannelId;

    @DecimalMin(
            value = "0.00",
            message = "Размер выплаты не может быть отрицательным"
    )
    private BigDecimal amount;

    private UUID frequencyId;

    private String documents;

    private LocalDate validFrom;

    private LocalDate validTo;

    @NotNull(message = "Статус проверки обязателен")
    private UUID verificationStatusId;

    private String actionUrl;

    public SupportMeasureRequest() {
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

    public UUID getSupportTypeId() {
        return supportTypeId;
    }

    public void setSupportTypeId(UUID supportTypeId) {
        this.supportTypeId = supportTypeId;
    }

    public UUID getLevelId() {
        return levelId;
    }

    public void setLevelId(UUID levelId) {
        this.levelId = levelId;
    }

    public UUID getRecipientTypeId() {
        return recipientTypeId;
    }

    public void setRecipientTypeId(UUID recipientTypeId) {
        this.recipientTypeId = recipientTypeId;
    }

    public Boolean getApplicationRequired() {
        return applicationRequired;
    }

    public void setApplicationRequired(Boolean applicationRequired) {
        this.applicationRequired = applicationRequired;
    }

    public UUID getApplicationChannelId() {
        return applicationChannelId;
    }

    public void setApplicationChannelId(UUID applicationChannelId) {
        this.applicationChannelId = applicationChannelId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(UUID frequencyId) {
        this.frequencyId = frequencyId;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public UUID getVerificationStatusId() {
        return verificationStatusId;
    }

    public void setVerificationStatusId(UUID verificationStatusId) {
        this.verificationStatusId = verificationStatusId;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}