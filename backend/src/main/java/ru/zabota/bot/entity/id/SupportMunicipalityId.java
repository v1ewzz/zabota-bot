package ru.zabota.bot.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

/*
 * Составной первичный ключ сущности SupportMunicipality.
 *
 * Соответствует составному PRIMARY KEY
 * (support_id, municipality_id)
 * таблицы support_municipality.
 */
@Embeddable
public class SupportMunicipalityId implements Serializable {

    @Column(name = "support_id", nullable = false)
    private UUID supportId;

    @Column(name = "municipality_id", nullable = false)
    private UUID municipalityId;

    public SupportMunicipalityId() {
    }

    public SupportMunicipalityId(UUID supportId, UUID municipalityId) {
        this.supportId = supportId;
        this.municipalityId = municipalityId;
    }

    public UUID getSupportId() {
        return supportId;
    }

    public void setSupportId(UUID supportId) {
        this.supportId = supportId;
    }

    public UUID getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(UUID municipalityId) {
        this.municipalityId = municipalityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SupportMunicipalityId that)) {
            return false;
        }

        return supportId.equals(that.supportId)
                && municipalityId.equals(that.municipalityId);
    }

    @Override
    public int hashCode() {
        return 31 * supportId.hashCode() + municipalityId.hashCode();
    }
}