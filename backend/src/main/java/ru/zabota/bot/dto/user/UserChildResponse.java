package ru.zabota.bot.dto.user;

import java.time.LocalDate;
import java.util.UUID;

/*
 * DTO данных ребёнка в полном профиле пользователя.
 */
public class UserChildResponse {

    private UUID childId;
    private LocalDate birthDate;
    private UUID educationLevelId;
    private String educationLevelCode;
    private String educationLevelName;
    private Short grade;
    private boolean disability;
    private UUID disabilityGroupId;
    private String disabilityGroupCode;
    private String disabilityGroupName;
    private boolean fullTime;
    private UUID institutionTypeId;
    private String institutionTypeCode;
    private String institutionTypeName;

    public UserChildResponse() {
    }

    public UUID getChildId() {
        return childId;
    }

    public void setChildId(UUID childId) {
        this.childId = childId;
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

    public String getEducationLevelCode() {
        return educationLevelCode;
    }

    public void setEducationLevelCode(String educationLevelCode) {
        this.educationLevelCode = educationLevelCode;
    }

    public String getEducationLevelName() {
        return educationLevelName;
    }

    public void setEducationLevelName(String educationLevelName) {
        this.educationLevelName = educationLevelName;
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

    public String getInstitutionTypeCode() {
        return institutionTypeCode;
    }

    public void setInstitutionTypeCode(String institutionTypeCode) {
        this.institutionTypeCode = institutionTypeCode;
    }

    public String getInstitutionTypeName() {
        return institutionTypeName;
    }

    public void setInstitutionTypeName(String institutionTypeName) {
        this.institutionTypeName = institutionTypeName;
    }
}
