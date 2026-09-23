package ru.zabota.bot.dto.npa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

/*
 * DTO для создания и обновления нормативно-правового акта.
 *
 * Связанные справочные значения передаются в виде UUID.
 * Идентификатор самого НПА и даты создания/изменения
 * устанавливаются сервером.
 */
public class NpaRequest {

        @NotBlank(message = "Название НПА обязательно")
        @Size(
                max = 500,
                message = "Название НПА не должно превышать 500 символов"
        )
        private String name;

        @NotNull(message = "Тип НПА обязателен")
        private UUID npaTypeId;

        @NotBlank(message = "Номер НПА обязателен")
        @Size(
                max = 100,
                message = "Номер НПА не должен превышать 100 символов"
        )
        private String number;

        @NotNull(message = "Дата принятия НПА обязательна")
        private LocalDate adoptionDate;

        private LocalDate validFrom;

        private LocalDate validTo;

        @NotNull(message = "Уровень НПА обязателен")
        private UUID levelId;

        @NotNull(message = "Статус НПА обязателен")
        private UUID statusId;

        @NotBlank(message = "Официальная ссылка обязательна")
        private String officialUrl;

        public NpaRequest() {
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public UUID getNpaTypeId() {
                return npaTypeId;
        }

        public void setNpaTypeId(UUID npaTypeId) {
                this.npaTypeId = npaTypeId;
        }

        public String getNumber() {
                return number;
        }

        public void setNumber(String number) {
                this.number = number;
        }

        public LocalDate getAdoptionDate() {
                return adoptionDate;
        }

        public void setAdoptionDate(LocalDate adoptionDate) {
                this.adoptionDate = adoptionDate;
        }

        public LocalDate getValidFrom() {
                return validFrom;
        }

        public void setValidFrom(LocalDate validFrom) {
                this.validFrom = validFrom;
        }

        public LocalDate getValidTo() {
                return validTo;
        }

        public void setValidTo(LocalDate validTo) {
                this.validTo = validTo;
        }

        public UUID getLevelId() {
                return levelId;
        }

        public void setLevelId(UUID levelId) {
                this.levelId = levelId;
        }

        public UUID getStatusId() {
                return statusId;
        }

        public void setStatusId(UUID statusId) {
                this.statusId = statusId;
        }

        public String getOfficialUrl() {
                return officialUrl;
        }

        public void setOfficialUrl(String officialUrl) {
                this.officialUrl = officialUrl;
        }
}