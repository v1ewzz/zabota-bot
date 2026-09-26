package ru.zabota.bot.dto.user;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/*
 * DTO данных ребёнка в анкете пользователя.
 */
public class UserChildRequest {

    @NotNull(message = "Дата рождения ребёнка обязательна")
    private LocalDate birthDate;

    @NotNull(message = "Уровень образования обязателен")
    private UUID educationLevelId;

    private Short grade;
    private boolean disability;
    private UUID disabilityGroupId;
    private boolean fullTime;
    private UUID institutionTypeId;

    public UserChildRequest() {
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public UUID getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(UUID educationLevelId) {
        this.educationLevelId = educationLevelId;
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

    public UUID getDisabilityGroupId() {
        return disabilityGroupId;
    }

    public void setDisabilityGroupId(UUID disabilityGroupId) {
        this.disabilityGroupId = disabilityGroupId;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    public UUID getInstitutionTypeId() {
        return institutionTypeId;
    }

    public void setInstitutionTypeId(UUID institutionTypeId) {
        this.institutionTypeId = institutionTypeId;
    }
}
