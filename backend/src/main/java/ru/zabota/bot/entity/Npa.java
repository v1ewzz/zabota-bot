package ru.zabota.bot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Сущность нормативно-правового акта.
 *
 * Соответствует таблице npa.
 * Хранит основные сведения о нормативном документе:
 * название, тип, номер, даты принятия и действия,
 * административный уровень, статус и официальную ссылку.
 *
 * Поля npaType, level и status являются ссылками
 * на значения справочника DictionaryValue.
 */
@Entity
@Table(name = "npa")
public class Npa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "npa_id", nullable = false, updatable = false)
    private UUID npaId;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "npa_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_npa_type")
    )
    private DictionaryValue npaType;

    @Column(name = "number", nullable = false, length = 100)
    private String number;

    @Column(name = "adoption_date", nullable = false)
    private LocalDate adoptionDate;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "level_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_npa_level")
    )
    private DictionaryValue level;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "status_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_npa_status")
    )
    private DictionaryValue status;

    @Column(name = "official_url", nullable = false, columnDefinition = "TEXT")
    private String officialUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Npa() {
    }

    public UUID getNpaId() {
        return npaId;
    }

    public void setNpaId(UUID npaId) {
        this.npaId = npaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DictionaryValue getNpaType() {
        return npaType;
    }

    public void setNpaType(DictionaryValue npaType) {
        this.npaType = npaType;
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

    public DictionaryValue getLevel() {
        return level;
    }

    public void setLevel(DictionaryValue level) {
        this.level = level;
    }

    public DictionaryValue getStatus() {
        return status;
    }

    public void setStatus(DictionaryValue status) {
        this.status = status;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}