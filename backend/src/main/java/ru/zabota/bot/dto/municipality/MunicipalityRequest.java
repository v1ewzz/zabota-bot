package ru.zabota.bot.dto.municipality;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/*
 * DTO для создания и обновления муниципального образования.
 *
 * Клиент передаёт идентификаторы региона и типа муниципального
 * образования, а также основные текстовые данные.
 */
public class MunicipalityRequest {

        @NotNull(message = "Регион обязателен")
        private UUID regionId;

        @NotBlank(message = "Название муниципального образования обязательно")
        @Size(
                max = 255,
                message = "Название муниципального образования не должно превышать 255 символов"
        )
        private String name;

        @Size(
                max = 255,
                message = "Район не должен превышать 255 символов"
        )
        private String district;

        private UUID typeId;

        public MunicipalityRequest() {
        }

        public UUID getRegionId() {
                return regionId;
        }

        public void setRegionId(UUID regionId) {
                this.regionId = regionId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getDistrict() {
                return district;
        }

        public void setDistrict(String district) {
                this.district = district;
        }

        public UUID getTypeId() {
                return typeId;
        }

        public void setTypeId(UUID typeId) {
                this.typeId = typeId;
        }
}