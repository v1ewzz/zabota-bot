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

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность пользователя.
 *
 * Соответствует таблице "user".
 * Хранит территориальные данные пользователя,
 * семейный и военный статус, признаки беременности,
 * травмы, инвалидности, жилищной и газификационной
 * потребности, а также сведения о занятости и доходе.
 *
 * Все справочные значения представлены ссылками
 * на DictionaryValue.
 */
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "region_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_region")
    )
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "municipality_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_municipality")
    )
    private Municipality municipality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "family_relation_id",
            foreignKey = @ForeignKey(name = "fk_user_family_relation")
    )
    private DictionaryValue familyRelation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "military_status_id",
            foreignKey = @ForeignKey(name = "fk_user_military_status")
    )
    private DictionaryValue militaryStatus;

    @Column(name = "pregnancy")
    private Boolean pregnancy;

    @Column(name = "injury", nullable = false)
    private boolean injury = false;

    @Column(name = "disability", nullable = false)
    private boolean disability = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "disability_group_id",
            foreignKey = @ForeignKey(name = "fk_user_disability_group")
    )
    private DictionaryValue disabilityGroup;

    @Column(name = "housing_problem", nullable = false)
    private boolean housingProblem = false;

    @Column(name = "gasification_needed", nullable = false)
    private boolean gasificationNeeded = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "employment_status_id",
            foreignKey = @ForeignKey(name = "fk_user_employment_status")
    )
    private DictionaryValue employmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "income_category_id",
            foreignKey = @ForeignKey(name = "fk_user_income_category")
    )
    private DictionaryValue incomeCategory;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public User() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public DictionaryValue getFamilyRelation() {
        return familyRelation;
    }

    public void setFamilyRelation(DictionaryValue familyRelation) {
        this.familyRelation = familyRelation;
    }

    public DictionaryValue getMilitaryStatus() {
        return militaryStatus;
    }

    public void setMilitaryStatus(DictionaryValue militaryStatus) {
        this.militaryStatus = militaryStatus;
    }

    public Boolean getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(Boolean pregnancy) {
        this.pregnancy = pregnancy;
    }

    public boolean isInjury() {
        return injury;
    }

    public void setInjury(boolean injury) {
        this.injury = injury;
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

    public boolean isHousingProblem() {
        return housingProblem;
    }

    public void setHousingProblem(boolean housingProblem) {
        this.housingProblem = housingProblem;
    }

    public boolean isGasificationNeeded() {
        return gasificationNeeded;
    }

    public void setGasificationNeeded(boolean gasificationNeeded) {
        this.gasificationNeeded = gasificationNeeded;
    }

    public DictionaryValue getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(DictionaryValue employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public DictionaryValue getIncomeCategory() {
        return incomeCategory;
    }

    public void setIncomeCategory(DictionaryValue incomeCategory) {
        this.incomeCategory = incomeCategory;
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