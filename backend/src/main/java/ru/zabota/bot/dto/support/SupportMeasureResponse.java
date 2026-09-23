package ru.zabota.bot.dto.support;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * DTO для возврата полной информации о мере социальной поддержки.
 *
 * Помимо идентификаторов справочных значений содержит их
 * машинные коды и отображаемые названия.
 */
public class SupportMeasureResponse {

    private UUID supportId;

    private String name;

    private String description;

    private UUID supportTypeId;
    private String supportTypeCode;
    private String supportTypeName;

    private UUID levelId;
    private String levelCode;
    private String levelName;

    private UUID recipientTypeId;
    private String recipientTypeCode;
    private String recipientTypeName;

    private boolean applicationRequired;

    private UUID applicationChannelId;
    private String applicationChannelCode;
    private String applicationChannelName;

    private BigDecimal amount;

    private UUID frequencyId;
    private String frequencyCode;
    private String frequencyName;

    private String documents;

    private LocalDate validFrom;
    private LocalDate validTo;

    private UUID verificationStatusId;
    private String verificationStatusCode;
    private String verificationStatusName;

    private String actionUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SupportMeasureResponse() {
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

    public UUID getSupportTypeId() {
        return supportTypeId;
    }

    public void setSupportTypeId(UUID supportTypeId) {
        this.supportTypeId = supportTypeId;
    }

    public String getSupportTypeCode() {
        return supportTypeCode;
    }

    public void setSupportTypeCode(String supportTypeCode) {
        this.supportTypeCode = supportTypeCode;
    }

    public String getSupportTypeName() {
        return supportTypeName;
    }

    public void setSupportTypeName(String supportTypeName) {
        this.supportTypeName = supportTypeName;
    }

    public UUID getLevelId() {
        return levelId;
    }

    public void setLevelId(UUID levelId) {
        this.levelId = levelId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public UUID getRecipientTypeId() {
        return recipientTypeId;
    }

    public void setRecipientTypeId(UUID recipientTypeId) {
        this.recipientTypeId = recipientTypeId;
    }

    public String getRecipientTypeCode() {
        return recipientTypeCode;
    }

    public void setRecipientTypeCode(String recipientTypeCode) {
        this.recipientTypeCode = recipientTypeCode;
    }

    public String getRecipientTypeName() {
        return recipientTypeName;
    }

    public void setRecipientTypeName(String recipientTypeName) {
        this.recipientTypeName = recipientTypeName;
    }

    public boolean isApplicationRequired() {
        return applicationRequired;
    }

    public void setApplicationRequired(boolean applicationRequired) {
        this.applicationRequired = applicationRequired;
    }

    public UUID getApplicationChannelId() {
        return applicationChannelId;
    }

    public void setApplicationChannelId(UUID applicationChannelId) {
        this.applicationChannelId = applicationChannelId;
    }

    public String getApplicationChannelCode() {
        return applicationChannelCode;
    }

    public void setApplicationChannelCode(String applicationChannelCode) {
        this.applicationChannelCode = applicationChannelCode;
    }

    public String getApplicationChannelName() {
        return applicationChannelName;
    }

    public void setApplicationChannelName(String applicationChannelName) {
        this.applicationChannelName = applicationChannelName;
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

    public String getFrequencyCode() {
        return frequencyCode;
    }

    public void setFrequencyCode(String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
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

    public String getVerificationStatusCode() {
        return verificationStatusCode;
    }

    public void setVerificationStatusCode(String verificationStatusCode) {
        this.verificationStatusCode = verificationStatusCode;
    }

    public String getVerificationStatusName() {
        return verificationStatusName;
    }

    public void setVerificationStatusName(String verificationStatusName) {
        this.verificationStatusName = verificationStatusName;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}