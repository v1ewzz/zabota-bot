package ru.zabota.bot.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * DTO основной информации о пользователе и сохранённых
 * параметров анкеты.
 */
public class UserResponse {

    private UUID userId;
    private String firstName;
    private String lastName;
    private UUID regionId;
    private String regionName;
    private UUID municipalityId;
    private String municipalityName;
    private UUID familyRelationId;
    private String familyRelationCode;
    private String familyRelationName;
    private UUID militaryStatusId;
    private String militaryStatusCode;
    private String militaryStatusName;
    private LocalDate birthDate;
    private Integer age;
    private Boolean pregnancy;
    private Short pregnancyDays;
    private UUID sexId;
    private String sexCode;
    private String sexName;
    private boolean injury;
    private boolean disability;
    private UUID disabilityGroupId;
    private String disabilityGroupCode;
    private String disabilityGroupName;
    private boolean housingProblem;
    private boolean gasificationNeeded;
    private UUID employmentStatusId;
    private String employmentStatusCode;
    private String employmentStatusName;
    private UUID incomeCategoryId;
    private String incomeCategoryCode;
    private String incomeCategoryName;
    private boolean loanExists;
    private boolean businessPlan;
    private boolean jobSeeker;
    private boolean socialServiceNeed;
    private LocalDate servicemanLeaveStart;
    private LocalDate servicemanLeaveEnd;
    private boolean servicemanOnLeave;
    private UUID legalIssueCategoryId;
    private String legalIssueCategoryCode;
    private String legalIssueCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponse() {
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public UUID getRegionId() { return regionId; }
    public void setRegionId(UUID regionId) { this.regionId = regionId; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public UUID getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(UUID municipalityId) { this.municipalityId = municipalityId; }
    public String getMunicipalityName() { return municipalityName; }
    public void setMunicipalityName(String municipalityName) { this.municipalityName = municipalityName; }
    public UUID getFamilyRelationId() { return familyRelationId; }
    public void setFamilyRelationId(UUID familyRelationId) { this.familyRelationId = familyRelationId; }
    public String getFamilyRelationCode() { return familyRelationCode; }
    public void setFamilyRelationCode(String familyRelationCode) { this.familyRelationCode = familyRelationCode; }
    public String getFamilyRelationName() { return familyRelationName; }
    public void setFamilyRelationName(String familyRelationName) { this.familyRelationName = familyRelationName; }
    public UUID getMilitaryStatusId() { return militaryStatusId; }
    public void setMilitaryStatusId(UUID militaryStatusId) { this.militaryStatusId = militaryStatusId; }
    public String getMilitaryStatusCode() { return militaryStatusCode; }
    public void setMilitaryStatusCode(String militaryStatusCode) { this.militaryStatusCode = militaryStatusCode; }
    public String getMilitaryStatusName() { return militaryStatusName; }
    public void setMilitaryStatusName(String militaryStatusName) { this.militaryStatusName = militaryStatusName; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Boolean getPregnancy() { return pregnancy; }
    public void setPregnancy(Boolean pregnancy) { this.pregnancy = pregnancy; }
    public Short getPregnancyDays() { return pregnancyDays; }
    public void setPregnancyDays(Short pregnancyDays) { this.pregnancyDays = pregnancyDays; }
    public UUID getSexId() { return sexId; }
    public void setSexId(UUID sexId) { this.sexId = sexId; }
    public String getSexCode() { return sexCode; }
    public void setSexCode(String sexCode) { this.sexCode = sexCode; }
    public String getSexName() { return sexName; }
    public void setSexName(String sexName) { this.sexName = sexName; }
    public boolean isInjury() { return injury; }
    public void setInjury(boolean injury) { this.injury = injury; }
    public boolean isDisability() { return disability; }
    public void setDisability(boolean disability) { this.disability = disability; }
    public UUID getDisabilityGroupId() { return disabilityGroupId; }
    public void setDisabilityGroupId(UUID disabilityGroupId) { this.disabilityGroupId = disabilityGroupId; }
    public String getDisabilityGroupCode() { return disabilityGroupCode; }
    public void setDisabilityGroupCode(String disabilityGroupCode) { this.disabilityGroupCode = disabilityGroupCode; }
    public String getDisabilityGroupName() { return disabilityGroupName; }
    public void setDisabilityGroupName(String disabilityGroupName) { this.disabilityGroupName = disabilityGroupName; }
    public boolean isHousingProblem() { return housingProblem; }
    public void setHousingProblem(boolean housingProblem) { this.housingProblem = housingProblem; }
    public boolean isGasificationNeeded() { return gasificationNeeded; }
    public void setGasificationNeeded(boolean gasificationNeeded) { this.gasificationNeeded = gasificationNeeded; }
    public UUID getEmploymentStatusId() { return employmentStatusId; }
    public void setEmploymentStatusId(UUID employmentStatusId) { this.employmentStatusId = employmentStatusId; }
    public String getEmploymentStatusCode() { return employmentStatusCode; }
    public void setEmploymentStatusCode(String employmentStatusCode) { this.employmentStatusCode = employmentStatusCode; }
    public String getEmploymentStatusName() { return employmentStatusName; }
    public void setEmploymentStatusName(String employmentStatusName) { this.employmentStatusName = employmentStatusName; }
    public UUID getIncomeCategoryId() { return incomeCategoryId; }
    public void setIncomeCategoryId(UUID incomeCategoryId) { this.incomeCategoryId = incomeCategoryId; }
    public String getIncomeCategoryCode() { return incomeCategoryCode; }
    public void setIncomeCategoryCode(String incomeCategoryCode) { this.incomeCategoryCode = incomeCategoryCode; }
    public String getIncomeCategoryName() { return incomeCategoryName; }
    public void setIncomeCategoryName(String incomeCategoryName) { this.incomeCategoryName = incomeCategoryName; }
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
    public boolean isServicemanOnLeave() { return servicemanOnLeave; }
    public void setServicemanOnLeave(boolean servicemanOnLeave) { this.servicemanOnLeave = servicemanOnLeave; }
    public UUID getLegalIssueCategoryId() { return legalIssueCategoryId; }
    public void setLegalIssueCategoryId(UUID legalIssueCategoryId) { this.legalIssueCategoryId = legalIssueCategoryId; }
    public String getLegalIssueCategoryCode() { return legalIssueCategoryCode; }
    public void setLegalIssueCategoryCode(String legalIssueCategoryCode) { this.legalIssueCategoryCode = legalIssueCategoryCode; }
    public String getLegalIssueCategoryName() { return legalIssueCategoryName; }
    public void setLegalIssueCategoryName(String legalIssueCategoryName) { this.legalIssueCategoryName = legalIssueCategoryName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
