package ru.zabota.bot.dto.support;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

/*
 * DTO для запуска подбора мер социальной поддержки.
 *
 * Содержит параметры, используемые алгоритмом подбора.
 * Значения справочников и территории передаются через UUID.
 *
 * Объект не является копией User Entity:
 * он описывает именно входные данные операции поиска.
 */
public class SupportSearchRequest {

    private UUID regionId;

    private UUID municipalityId;

    private UUID familyRelationId;

    private UUID militaryStatusId;

    private Boolean pregnancy;

    private boolean injury;

    private boolean disability;

    private UUID disabilityGroupId;

    private boolean housingProblem;

    private boolean gasificationNeeded;

    private UUID employmentStatusId;

    private UUID incomeCategoryId;

    @Valid
    private List<ru.zabota.bot.dto.user.UserChildRequest> children;

    public SupportSearchRequest() {
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void setRegionId(UUID regionId) {
        this.regionId = regionId;
    }

    public UUID getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(UUID municipalityId) {
        this.municipalityId = municipalityId;
    }

    public UUID getFamilyRelationId() {
        return familyRelationId;
    }

    public void setFamilyRelationId(UUID familyRelationId) {
        this.familyRelationId = familyRelationId;
    }

    public UUID getMilitaryStatusId() {
        return militaryStatusId;
    }

    public void setMilitaryStatusId(UUID militaryStatusId) {
        this.militaryStatusId = militaryStatusId;
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

    public UUID getIncomeCategoryId() {
        return incomeCategoryId;
    }

    public void setIncomeCategoryId(UUID incomeCategoryId) {
        this.incomeCategoryId = incomeCategoryId;
    }

    public List<ru.zabota.bot.dto.user.UserChildRequest> getChildren() {
        return children;
    }

    public void setChildren(List<ru.zabota.bot.dto.user.UserChildRequest> children) {
        this.children = children;
    }
}