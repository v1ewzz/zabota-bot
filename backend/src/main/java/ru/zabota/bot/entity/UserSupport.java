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
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность результата подбора меры социальной поддержки
 * для конкретного пользователя.
 *
 * Соответствует таблице user_support.
 * Хранит связь пользователя с мерой поддержки,
 * текущий статус, выбранное действие, даты прохождения
 * этапов обращения и дополнительную заметку.
 */
@Entity
@Table(
        name = "user_support",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_support_user_support",
                        columnNames = {"user_id", "support_id"}
                )
        }
)
public class UserSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_support_id", nullable = false, updatable = false)
    private UUID userSupportId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_support_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "support_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_support_support")
    )
    private SupportMeasure support;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "status_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_support_status")
    )
    private DictionaryValue status;

    @Column(name = "selected_for_action", nullable = false)
    private boolean selectedForAction = false;

    @CreationTimestamp
    @Column(name = "checked_at", nullable = false, updatable = false)
    private LocalDateTime checkedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    public UserSupport() {
    }

    public UUID getUserSupportId() {
        return userSupportId;
    }

    public void setUserSupportId(UUID userSupportId) {
        this.userSupportId = userSupportId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SupportMeasure getSupport() {
        return support;
    }

    public void setSupport(SupportMeasure support) {
        this.support = support;
    }

    public DictionaryValue getStatus() {
        return status;
    }

    public void setStatus(DictionaryValue status) {
        this.status = status;
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
