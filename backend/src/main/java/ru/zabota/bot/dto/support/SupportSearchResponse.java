package ru.zabota.bot.dto.support;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * DTO результата подбора мер социальной поддержки.
 *
 * Содержит идентификатор пользователя, для которого выполнялся
 * подбор, и список найденных подходящих мер.
 */
public class SupportSearchResponse {

    private UUID userId;

    private List<SupportMeasureShortResponse> measures = new ArrayList<>();

    public SupportSearchResponse() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<SupportMeasureShortResponse> getMeasures() {
        return measures;
    }

    public void setMeasures(List<SupportMeasureShortResponse> measures) {
        this.measures = measures;
    }
}