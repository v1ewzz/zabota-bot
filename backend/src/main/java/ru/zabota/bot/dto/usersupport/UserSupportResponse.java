package ru.zabota.bot.dto.usersupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * DTO персональной меры поддержки пользователя.
 *
 * Содержит данные меры, текущий статус оформления
 * и персональные даты взаимодействия с мерой.
 *
 * DTO не раскрывает внутренние правила подбора,
 * НПА и другие служебные данные.
 */
public class UserSupportResponse {

    private UUID userSupportId;
    private UUID supportId;
    private String name;
    private String description;
    private BigDecimal amount;
    private String frequency;
    private boolean applicationRequired;
    private String applicationChannel;
    private String actionUrl;

    private UUID statusId;
    private String statusCode;
    private String statusName;

    private boolean selectedForAction;
    private LocalDateTime checkedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime receivedAt;
    private String note;

    public UserSupportResponse() {
    }

    public UUID getUserSupportId() {
        return userSupportId;
    }

    public void setUserSupportId(UUID userSupportId) {
        this.userSupportId = userSupportId;
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

    public UUID getStatusId() {
        return statusId;
    }

    public void setStatusId(UUID statusId) {
        this.statusId = statusId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public boolean isSelectedForAction() {
        return selectedForAction;
    }

    public void setSelectedForAction(boolean selectedForAction) {
        this.selectedForAction = selectedForAction;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
