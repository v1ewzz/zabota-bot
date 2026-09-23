package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.region.RegionRequest;
import ru.zabota.bot.dto.region.RegionResponse;
import ru.zabota.bot.entity.Region;

/*
 * Маппер сущности Region.
 *
 * Отвечает за преобразование:
 *
 * RegionRequest → Region
 * Region → RegionResponse
 *
 * Также выполняет обновление существующей сущности
 * данными из RegionRequest.
 */
@Component
public class RegionMapper {

    public Region toEntity(RegionRequest request) {
        if (request == null) {
            return null;
        }

        Region region = new Region();

        updateEntity(region, request);

        return region;
    }

    public void updateEntity(
            Region region,
            RegionRequest request
    ) {
        if (region == null || request == null) {
            return;
        }

        region.setName(request.getName());
        region.setCode(request.getCode());
    }

    public RegionResponse toResponse(Region region) {
        if (region == null) {
            return null;
        }

        RegionResponse response = new RegionResponse();

        response.setRegionId(region.getRegionId());
        response.setName(region.getName());
        response.setCode(region.getCode());
        response.setCreatedAt(region.getCreatedAt());

        return response;
    }
}