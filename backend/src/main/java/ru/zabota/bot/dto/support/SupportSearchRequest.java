package ru.zabota.bot.dto.support;

import jakarta.validation.Valid;
import ru.zabota.bot.dto.user.UserChildRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/*
 * DTO входных данных операции персонального подбора.
 *
 * Содержит все параметры сохранённой анкеты, которые могут
 * использоваться rules engine.
 */
public class SupportSearchRequest {

    private UUID regionId;
    private UUID municipalityId;
    private UUID familyRelationId;
    private UUID militaryStatusId;
    private LocalDate birthDate;
    private Boolean pregnancy;
    private Short pregnancyDays;
    private UUID sexId;
    private boolean injury;
    private boolean disability;
    private UUID disabilityGroupId;
    private boolean housingProblem;
    private boolean gasificationNeeded;
    private UUID employmentStatusId;
    private UUID incomeCategoryId;
    private boolean loanExists;
    private boolean businessPlan;
    private boolean jobSeeker;
    private boolean socialServiceNeed;
    private LocalDate servicemanLeaveStart;
    private LocalDate servicemanLeaveEnd;
    private UUID legalIssueCategoryId;

    @Valid
    private List<UserChildRequest> children;

    public SupportSearchRequest() {
    }

    public UUID getRegionId() { return regionId; }
    public void setRegionId(UUID regionId) { this.regionId = regionId; }
    public UUID getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(UUID municipalityId) { this.municipalityId = municipalityId; }
    public UUID getFamilyRelationId() { return familyRelationId; }
    public void setFamilyRelationId(UUID familyRelationId) { this.familyRelationId = familyRelationId; }
    public UUID getMilitaryStatusId() { return militaryStatusId; }
    public void setMilitaryStatusId(UUID militaryStatusId) { this.militaryStatusId = militaryStatusId; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public Boolean getPregnancy() { return pregnancy; }
    public void setPregnancy(Boolean pregnancy) { this.pregnancy = pregnancy; }
    public Short getPregnancyDays() { return pregnancyDays; }
    public void setPregnancyDays(Short pregnancyDays) { this.pregnancyDays = pregnancyDays; }
    public UUID getSexId() { return sexId; }
    public void setSexId(UUID sexId) { this.sexId = sexId; }
    public boolean isInjury() { return injury; }
    public void setInjury(boolean injury) { this.injury = injury; }
    public boolean isDisability() { return disability; }
    public void setDisability(boolean disability) { this.disability = disability; }
    public UUID getDisabilityGroupId() { return disabilityGroupId; }
    public void setDisabilityGroupId(UUID disabilityGroupId) { this.disabilityGroupId = disabilityGroupId; }
    public boolean isHousingProblem() { return housingProblem; }
    public void setHousingProblem(boolean housingProblem) { this.housingProblem = housingProblem; }
    public boolean isGasificationNeeded() { return gasificationNeeded; }
    public void setGasificationNeeded(boolean gasificationNeeded) { this.gasificationNeeded = gasificationNeeded; }
    public UUID getEmploymentStatusId() { return employmentStatusId; }
    public void setEmploymentStatusId(UUID employmentStatusId) { this.employmentStatusId = employmentStatusId; }
    public UUID getIncomeCategoryId() { return incomeCategoryId; }
    public void setIncomeCategoryId(UUID incomeCategoryId) { this.incomeCategoryId = incomeCategoryId; }
    public boolean isLoanExists() { return loanExists; }
    public void setLoanExists(boolean loanExists) { this.loanExists = loanExists; }
    public boolean isBusinessPlan() { return businessPlan; }
    public void setBusinessPlan(boolean businessPlan) { this.businessPlan = businessPlan; }
    public boolean isJobSeeker() { return jobSeeker; }
    public void setJobSeeker(boolean jobSeeker) { this.jobSeeker = jobSeeker; }
    public boolean isSocialServiceNeed() { return socialServiceNeed; }
    public void setSocialServiceNeed(boolean socialServiceNeed) { this.socialServiceNeed = socialServiceNeed; }
    public LocalDate getServicemanLeaveStart() { return servicemanLeaveStart; }
    public void setServicemanLeaveStart(LocalDate servicemanLeaveStart) { this.servicemanLeaveStart = servicemanLeaveStart; }
    public LocalDate getServicemanLeaveEnd() { return servicemanLeaveEnd; }
    public void setServicemanLeaveEnd(LocalDate servicemanLeaveEnd) { this.servicemanLeaveEnd = servicemanLeaveEnd; }
    public UUID getLegalIssueCategoryId() { return legalIssueCategoryId; }
    public void setLegalIssueCategoryId(UUID legalIssueCategoryId) { this.legalIssueCategoryId = legalIssueCategoryId; }
    public List<UserChildRequest> getChildren() { return children; }
    public void setChildren(List<UserChildRequest> children) { this.children = children; }
}
