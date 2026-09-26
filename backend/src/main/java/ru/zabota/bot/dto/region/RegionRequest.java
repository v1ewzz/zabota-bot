package ru.zabota.bot.dto.region;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * DTO для создания и обновления региона.
 *
 * Содержит только те поля, которые клиент может передавать
 * при создании или изменении региона.
 */
public class RegionRequest {

        @NotBlank(message = "Название региона обязательно")
        @Size(
                max = 255,
                message = "Название региона не должно превышать 255 символов"
        )
        private String name;

        @Size(
                max = 50,
                message = "Код региона не должен превышать 50 символов"
        )
        private String code;

        public RegionRequest() {
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
        }
}