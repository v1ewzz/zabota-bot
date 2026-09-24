package ru.zabota.bot.dto.municipality;

import java.time.LocalDateTime;
import java.util.UUID;

/*
 * DTO для возврата информации о муниципальном образовании.
 *
 * Содержит данные самого муниципального образования,
 * а также основные сведения о связанном регионе и типе.
 */
public class MunicipalityResponse {

    private UUID municipalityId;

    private UUID regionId;
    private String regionName;

    private String name;
    private String district;

    private UUID typeId;
    private String typeCode;
    private String typeName;

    private LocalDateTime createdAt;

    public MunicipalityResponse() {
    }

    public UUID getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(UUID municipalityId) {
        this.municipalityId = municipalityId;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public void setRegionId(UUID regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}