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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность пользователя и сохранённой анкеты.
 *
 * Хранит базовый профиль, территорию, семейную роль,
 * военный статус, медицинские, жилищные, трудовые,
 * финансовые и дополнительные признаки, используемые rules engine.
 * Все справочные значения представлены ссылками на DictionaryValue.
 */
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_region"))
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipality_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_municipality"))
    private Municipality municipality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_relation_id", foreignKey = @ForeignKey(name = "fk_user_family_relation"))
    private DictionaryValue familyRelation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "military_status_id", foreignKey = @ForeignKey(name = "fk_user_military_status"))
    private DictionaryValue militaryStatus;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "pregnancy")
    private Boolean pregnancy;

    @Column(name = "pregnancy_days")
    private Short pregnancyDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sex_id", foreignKey = @ForeignKey(name = "fk_user_sex"))
    private DictionaryValue sex;

    @Column(name = "injury", nullable = false)
    private boolean injury;

    @Column(name = "disability", nullable = false)
    private boolean disability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disability_group_id", foreignKey = @ForeignKey(name = "fk_user_disability_group"))
    private DictionaryValue disabilityGroup;

    @Column(name = "housing_problem", nullable = false)
    private boolean housingProblem;

    @Column(name = "gasification_needed", nullable = false)
    private boolean gasificationNeeded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_status_id", foreignKey = @ForeignKey(name = "fk_user_employment_status"))
    private DictionaryValue employmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_category_id", foreignKey = @ForeignKey(name = "fk_user_income_category"))
    private DictionaryValue incomeCategory;

    @Column(name = "loan_exists", nullable = false)
    private boolean loanExists;

    @Column(name = "business_plan", nullable = false)
    private boolean businessPlan;

    @Column(name = "job_seeker", nullable = false)
    private boolean jobSeeker;

    @Column(name = "social_service_need", nullable = false)
    private boolean socialServiceNeed;

    @Column(name = "serviceman_leave_start")
    private LocalDate servicemanLeaveStart;

    @Column(name = "serviceman_leave_end")
    private LocalDate servicemanLeaveEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_issue_category_id", foreignKey = @ForeignKey(name = "fk_user_legal_issue_category"))
    private DictionaryValue legalIssueCategory;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(Boolean pregnancy) {
        this.pregnancy = pregnancy;
    }

    public Short getPregnancyDays() {
        return pregnancyDays;
    }

    public void setPregnancyDays(Short pregnancyDays) {
        this.pregnancyDays = pregnancyDays;
    }

    public DictionaryValue getSex() {
        return sex;
    }

    public void setSex(DictionaryValue sex) {
        this.sex = sex;
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

    public boolean isLoanExists() {
        return loanExists;
    }

    public void setLoanExists(boolean loanExists) {
        this.loanExists = loanExists;
    }

    public boolean isBusinessPlan() {
        return businessPlan;
    }

    public void setBusinessPlan(boolean businessPlan) {
        this.businessPlan = businessPlan;
    }

    public boolean isJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(boolean jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public boolean isSocialServiceNeed() {
        return socialServiceNeed;
    }

    public void setSocialServiceNeed(boolean socialServiceNeed) {
        this.socialServiceNeed = socialServiceNeed;
    }

    public LocalDate getServicemanLeaveStart() {
        return servicemanLeaveStart;
    }

    public void setServicemanLeaveStart(LocalDate servicemanLeaveStart) {
        this.servicemanLeaveStart = servicemanLeaveStart;
    }

    public LocalDate getServicemanLeaveEnd() {
        return servicemanLeaveEnd;
    }

    public void setServicemanLeaveEnd(LocalDate servicemanLeaveEnd) {
        this.servicemanLeaveEnd = servicemanLeaveEnd;
    }

    public DictionaryValue getLegalIssueCategory() {
        return legalIssueCategory;
    }

    public void setLegalIssueCategory(DictionaryValue legalIssueCategory) {
        this.legalIssueCategory = legalIssueCategory;
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
