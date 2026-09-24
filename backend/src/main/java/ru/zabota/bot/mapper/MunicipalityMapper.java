package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.municipality.MunicipalityRequest;
import ru.zabota.bot.dto.municipality.MunicipalityResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;

/*
 * Маппер сущности Municipality.
 *
 * Отвечает за ручное преобразование:
 *
 * MunicipalityRequest → Municipality
 * Municipality → MunicipalityResponse
 *
 * Связанные Region и DictionaryValue передаются
 * из сервисного слоя.
 */
@Component
public class MunicipalityMapper {

    public Municipality toEntity(
            MunicipalityRequest request,
            Region region,
            DictionaryValue type
    ) {
        if (request == null) {
            return null;
        }

        Municipality municipality = new Municipality();

        updateEntity(
                municipality,
                request,
                region,
                type
        );

        return municipality;
    }

    public void updateEntity(
            Municipality municipality,
            MunicipalityRequest request,
            Region region,
            DictionaryValue type
    ) {
        if (municipality == null || request == null) {
            return;
        }

        municipality.setRegion(region);
        municipality.setName(request.getName());
        municipality.setDistrict(request.getDistrict());
        municipality.setType(type);
    }

    public MunicipalityResponse toResponse(Municipality municipality) {
        if (municipality == null) {
            return null;
        }

        MunicipalityResponse response = new MunicipalityResponse();

        response.setMunicipalityId(municipality.getMunicipalityId());
        response.setName(municipality.getName());
        response.setDistrict(municipality.getDistrict());
        response.setCreatedAt(municipality.getCreatedAt());

        Region region = municipality.getRegion();

        if (region != null) {
            response.setRegionId(region.getRegionId());
            response.setRegionName(region.getName());
        }

        DictionaryValue type = municipality.getType();

        if (type != null) {
            response.setTypeId(type.getDictionaryValueId());
            response.setTypeCode(type.getCode());
            response.setTypeName(type.getLabel());
        }

        return response;
    }
}