package ru.zabota.bot.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

/*
 * Составной первичный ключ сущности SupportNpa.
 *
 * Соответствует составному PRIMARY KEY (support_id, npa_id)
 * таблицы support_npa.
 */
@Embeddable
public class SupportNpaId implements Serializable {

    @Column(name = "support_id", nullable = false)
    private UUID supportId;

    @Column(name = "npa_id", nullable = false)
    private UUID npaId;

    public SupportNpaId() {
    }

    public SupportNpaId(UUID supportId, UUID npaId) {
        this.supportId = supportId;
        this.npaId = npaId;
    }

    public UUID getSupportId() {
        return supportId;
    }

    public void setSupportId(UUID supportId) {
        this.supportId = supportId;
    }

    public UUID getNpaId() {
        return npaId;
    }

    public void setNpaId(UUID npaId) {
        this.npaId = npaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SupportNpaId that)) {
            return false;
        }

        return supportId.equals(that.supportId)
                && npaId.equals(that.npaId);
    }

    @Override
    public int hashCode() {
        return 31 * supportId.hashCode() + npaId.hashCode();
    }
}