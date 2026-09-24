package ru.zabota.bot.dto.usersupport;

import jakarta.validation.constraints.Size;

import java.util.UUID;

/*
 * DTO для изменения персонального результата подбора.
 *
 * Все поля необязательны по отдельности, но хотя бы одно
 * изменение должно быть передано.
 */
public class UserSupportUpdateRequest {

    private UUID statusId;
    private Boolean selectedForAction;

    @Size(max = 2000, message = "Заметка не должна превышать 2000 символов")
    private String note;

    public UserSupportUpdateRequest() {
    }

    public UUID getStatusId() {
        return statusId;
    }

    public void setStatusId(UUID statusId) {
        this.statusId = statusId;
    }

    public Boolean getSelectedForAction() {
        return selectedForAction;
    }

    public void setSelectedForAction(Boolean selectedForAction) {
        this.selectedForAction = selectedForAction;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
