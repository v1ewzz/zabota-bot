package ru.zabota.bot.dto.npa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * DTO для возврата информации о нормативно-правовом акте.
 *
 * Содержит основные данные НПА, а также идентификаторы,
 * коды и отображаемые значения связанных справочников.
 */
public class NpaResponse {

    private UUID npaId;

    private String name;

    private UUID npaTypeId;
    private String npaTypeCode;
    private String npaTypeName;

    private String number;

    private LocalDate adoptionDate;
    private LocalDate validFrom;
    private LocalDate validTo;

    private UUID levelId;
    private String levelCode;
    private String levelName;

    private UUID statusId;
    private String statusCode;
    private String statusName;

    private String officialUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NpaResponse() {
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

    public UUID getNpaTypeId() {
        return npaTypeId;
    }

    public void setNpaTypeId(UUID npaTypeId) {
        this.npaTypeId = npaTypeId;
    }

    public String getNpaTypeCode() {
        return npaTypeCode;
    }

    public void setNpaTypeCode(String npaTypeCode) {
        this.npaTypeCode = npaTypeCode;
    }

    public String getNpaTypeName() {
        return npaTypeName;
    }

    public void setNpaTypeName(String npaTypeName) {
        this.npaTypeName = npaTypeName;
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

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public UUID getStatusId() {
        return statusId;
    }

    public void setStatusId(UUID statusId) {
        this.statusId = statusId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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