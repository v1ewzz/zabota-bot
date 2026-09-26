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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность ребёнка пользователя.
 *
 * Хранит дату рождения, образование, класс, инвалидность,
 * форму обучения и тип образовательной организации.
 */
@Entity
@Table(name = "user_child")
public class UserChild {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "child_id", nullable = false, updatable = false)
    private UUID childId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_child_user"))
    private User user;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "education_level_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_child_education_level"))
    private DictionaryValue educationLevel;

    @Column(name = "grade")
    private Short grade;

    @Column(name = "disability", nullable = false)
    private boolean disability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disability_group_id", foreignKey = @ForeignKey(name = "fk_user_child_disability_group"))
    private DictionaryValue disabilityGroup;

    @Column(name = "full_time", nullable = false)
    private boolean fullTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_type_id", foreignKey = @ForeignKey(name = "fk_user_child_institution_type"))
    private DictionaryValue institutionType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserChild() {
    }

    public UUID getChildId() {
        return childId;
    }

    public void setChildId(UUID childId) {
        this.childId = childId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public DictionaryValue getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(DictionaryValue educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Short getGrade() {
        return grade;
    }

    public void setGrade(Short grade) {
        this.grade = grade;
    }

    public boolean isDisability() {
        return disability;
    }

    public void setDisability(boolean disability) {
        this.disability = disability;
    }

    public DictionaryValue getDisabilityGroup() {
        return disabilityGroup;
    }

    public void setDisabilityGroup(DictionaryValue disabilityGroup) {
        this.disabilityGroup = disabilityGroup;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    public DictionaryValue getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(DictionaryValue institutionType) {
        this.institutionType = institutionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
