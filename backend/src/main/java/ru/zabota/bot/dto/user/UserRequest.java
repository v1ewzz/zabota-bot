package ru.zabota.bot.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

/*

 * DTO для создания и обновления профиля пользователя.
 *
 * Содержит территориальные данные, социальные признаки,
 * справочные значения и список детей.
 *
 * Справочные и территориальные сущности передаются
 * через UUID, а не через JPA Entity.
 */
public class UserRequest {

        @NotBlank(message = "Имя обязательно")
        @Size(max = 100, message = "Имя не должно превышать 100 символов")
        private String firstName;

        @NotBlank(message = "Фамилия обязательна")
        @Size(max = 100, message = "Фамилия не должна превышать 100 символов")
        private String lastName;

        @NotNull(message = "Регион обязателен")
        private UUID regionId;

        @NotNull(message = "Муниципалитет обязателен")
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
        private List<UserChildRequest> children;

        public UserRequest() {
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

        public UUID getRegionId() {
                return regionId;
        }

        public boolean isGasificationNeeded() {
                return gasificationNeeded;
        }

        public void setGasificationNeeded(boolean gasificationNeeded) {
                this.gasificationNeeded = gasificationNeeded;
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

        public List<UserChildRequest> getChildren() {
                return children;
        }

        public void setChildren(List<UserChildRequest> children) {
                this.children = children;
        }
}
