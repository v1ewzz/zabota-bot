package ru.zabota.bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import ru.zabota.bot.entity.id.SupportMunicipalityId;

import java.time.LocalDate;
import java.util.UUID;

/*
 * Сущность связи между мерой социальной поддержки
 * и муниципальным образованием.
 *
 * Соответствует таблице support_municipality.
 * Реализует связь M:N между SupportMeasure и Municipality.
 *
 * Дополнительно хранит период действия меры поддержки
 * на конкретной территории.
 */
@Entity
@Table(name = "support_municipality")
public class SupportMunicipality {

    @EmbeddedId
    private SupportMunicipalityId id;

    @MapsId("supportId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "support_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_municipality_support")
    )
    private SupportMeasure support;

    @MapsId("municipalityId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "municipality_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_municipality_municipality")
    )
    private Municipality municipality;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    public SupportMunicipality() {
    }

    public SupportMunicipalityId getId() {
        return id;
    }

    public void setId(SupportMunicipalityId id) {
        this.id = id;
    }

    public SupportMeasure getSupport() {
        return support;
    }

    public void setSupport(SupportMeasure support) {
        this.support = support;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
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
}