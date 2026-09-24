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
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность меры социальной поддержки.
 *
 * Соответствует таблице support_measure.
 * Хранит основную информацию о мере поддержки, её типе,
 * уровне предоставления, категории получателя, способе
 * оформления, размере, периодичности, документах,
 * сроке действия и статусе проверки.
 *
 * Справочные значения представлены ссылками
 * на DictionaryValue.
 */
@Entity
@Table(name = "support_measure")
public class SupportMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "support_id", nullable = false, updatable = false)
    private UUID supportId;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "support_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_measure_type")
    )
    private DictionaryValue supportType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "level_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_measure_level")
    )
    private DictionaryValue level;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "recipient_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_measure_recipient_type")
    )
    private DictionaryValue recipientType;

    @Column(name = "application_required", nullable = false)
    private boolean applicationRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "application_channel_id",
            foreignKey = @ForeignKey(name = "fk_support_measure_application_channel")
    )
    private DictionaryValue applicationChannel;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "frequency_id",
            foreignKey = @ForeignKey(name = "fk_support_measure_frequency")
    )
    private DictionaryValue frequency;

    @Column(name = "documents", columnDefinition = "TEXT")
    private String documents;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "verification_status_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_measure_verification_status")
    )
    private DictionaryValue verificationStatus;

    @Column(name = "action_url", columnDefinition = "TEXT")
    private String actionUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SupportMeasure() {
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

    public DictionaryValue getSupportType() {
        return supportType;
    }

    public void setSupportType(DictionaryValue supportType) {
        this.supportType = supportType;
    }

    public DictionaryValue getLevel() {
        return level;
    }

    public void setLevel(DictionaryValue level) {
        this.level = level;
    }

    public DictionaryValue getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(DictionaryValue recipientType) {
        this.recipientType = recipientType;
    }

    public boolean isApplicationRequired() {
        return applicationRequired;
    }

    public void setApplicationRequired(boolean applicationRequired) {
        this.applicationRequired = applicationRequired;
    }

    public DictionaryValue getApplicationChannel() {
        return applicationChannel;
    }

    public void setApplicationChannel(DictionaryValue applicationChannel) {
        this.applicationChannel = applicationChannel;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DictionaryValue getFrequency() {
        return frequency;
    }

    public void setFrequency(DictionaryValue frequency) {
        this.frequency = frequency;
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

    public DictionaryValue getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(DictionaryValue verificationStatus) {
        this.verificationStatus = verificationStatus;
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