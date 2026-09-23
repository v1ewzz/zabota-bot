package ru.zabota.bot.dto.user;

import java.time.LocalDateTime;
import java.util.UUID;

/*

 * DTO для возврата основной информации о пользователе.
 *
 * Содержит идентификаторы связанных сущностей и их
 * основные отображаемые значения.
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

    private Boolean pregnancy;

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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponse() {
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void setRegionId(UUID regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public UUID getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(UUID municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public UUID getFamilyRelationId() {
        return familyRelationId;
    }

    public void setFamilyRelationId(UUID familyRelationId) {
        this.familyRelationId = familyRelationId;
    }

    public String getFamilyRelationCode() {
        return familyRelationCode;
    }

    public void setFamilyRelationCode(String familyRelationCode) {
        this.familyRelationCode = familyRelationCode;
    }

    public String getFamilyRelationName() {
        return familyRelationName;
    }

    public void setFamilyRelationName(String familyRelationName) {
        this.familyRelationName = familyRelationName;
    }

    public UUID getMilitaryStatusId() {
        return militaryStatusId;
    }

    public void setMilitaryStatusId(UUID militaryStatusId) {
        this.militaryStatusId = militaryStatusId;
    }

    public String getMilitaryStatusCode() {
        return militaryStatusCode;
    }

    public void setMilitaryStatusCode(String militaryStatusCode) {
        this.militaryStatusCode = militaryStatusCode;
    }

    public String getMilitaryStatusName() {
        return militaryStatusName;
    }

    public void setMilitaryStatusName(String militaryStatusName) {
        this.militaryStatusName = militaryStatusName;
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

    public UUID getDisabilityGroupId() {
        return disabilityGroupId;
    }

    public void setDisabilityGroupId(UUID disabilityGroupId) {
        this.disabilityGroupId = disabilityGroupId;
    }

    public String getDisabilityGroupCode() {
        return disabilityGroupCode;
    }

    public void setDisabilityGroupCode(String disabilityGroupCode) {
        this.disabilityGroupCode = disabilityGroupCode;
    }

    public String getDisabilityGroupName() {
        return disabilityGroupName;
    }

    public void setDisabilityGroupName(String disabilityGroupName) {
        this.disabilityGroupName = disabilityGroupName;
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

    public UUID getEmploymentStatusId() {
        return employmentStatusId;
    }

    public void setEmploymentStatusId(UUID employmentStatusId) {
        this.employmentStatusId = employmentStatusId;
    }

    public String getEmploymentStatusCode() {
        return employmentStatusCode;
    }

    public void setEmploymentStatusCode(String employmentStatusCode) {
        this.employmentStatusCode = employmentStatusCode;
    }

    public String getEmploymentStatusName() {
        return employmentStatusName;
    }

    public void setEmploymentStatusName(String employmentStatusName) {
        this.employmentStatusName = employmentStatusName;
    }

    public UUID getIncomeCategoryId() {
        return incomeCategoryId;
    }

    public void setIncomeCategoryId(UUID incomeCategoryId) {
        this.incomeCategoryId = incomeCategoryId;
    }

    public String getIncomeCategoryCode() {
        return incomeCategoryCode;
    }

    public void setIncomeCategoryCode(String incomeCategoryCode) {
        this.incomeCategoryCode = incomeCategoryCode;
    }

    public String getIncomeCategoryName() {
        return incomeCategoryName;
    }

    public void setIncomeCategoryName(String incomeCategoryName) {
        this.incomeCategoryName = incomeCategoryName;
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
