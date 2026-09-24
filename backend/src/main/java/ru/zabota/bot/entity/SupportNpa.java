package ru.zabota.bot.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import ru.zabota.bot.entity.id.SupportNpaId;


/*
 * Сущность связи между мерой социальной поддержки и НПА.
 *
 * Соответствует таблице support_npa.
 * Реализует связь M:N между SupportMeasure и Npa.
 *
 * Составной первичный ключ состоит из support_id и npa_id.
 * Дополнительно хранится relation_type_id, определяющий
 * характер связи меры поддержки с нормативным актом.
 */
@Entity
@Table(name = "support_npa")
public class SupportNpa {

    @EmbeddedId
    private SupportNpaId id;

    @MapsId("supportId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "support_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_npa_support")
    )
    private SupportMeasure support;

    @MapsId("npaId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "npa_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_npa_npa")
    )
    private Npa npa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "relation_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_support_npa_relation_type")
    )
    private DictionaryValue relationType;

    public SupportNpa() {
    }

    public SupportNpaId getId() {
        return id;
    }

    public void setId(SupportNpaId id) {
        this.id = id;
    }

    public SupportMeasure getSupport() {
        return support;
    }

    public void setSupport(SupportMeasure support) {
        this.support = support;
    }

    public Npa getNpa() {
        return npa;
    }

    public void setNpa(Npa npa) {
        this.npa = npa;
    }

    public DictionaryValue getRelationType() {
        return relationType;
    }

    public void setRelationType(DictionaryValue relationType) {
        this.relationType = relationType;
    }
}