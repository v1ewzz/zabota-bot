package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.npa.NpaRequest;
import ru.zabota.bot.dto.npa.NpaResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Npa;

/*
 * Маппер сущности Npa.
 *
 * Отвечает за преобразование:
 *
 * NpaRequest → Npa
 * Npa → NpaResponse
 *
 * Связанные DictionaryValue передаются из сервисного слоя,
 * поскольку именно сервис отвечает за разрешение внешних ключей.
 */
@Component
public class NpaMapper {

    public Npa toEntity(
            NpaRequest request,
            DictionaryValue npaType,
            DictionaryValue level,
            DictionaryValue status
    ) {
        if (request == null) {
            return null;
        }

        Npa npa = new Npa();

        updateEntity(
                npa,
                request,
                npaType,
                level,
                status
        );

        return npa;
    }

    public void updateEntity(
            Npa npa,
            NpaRequest request,
            DictionaryValue npaType,
            DictionaryValue level,
            DictionaryValue status
    ) {
        if (npa == null || request == null) {
            return;
        }

        npa.setName(request.getName());
        npa.setNpaType(npaType);
        npa.setNumber(request.getNumber());
        npa.setAdoptionDate(request.getAdoptionDate());
        npa.setValidFrom(request.getValidFrom());
        npa.setValidTo(request.getValidTo());
        npa.setLevel(level);
        npa.setStatus(status);
        npa.setOfficialUrl(request.getOfficialUrl());
    }

    public NpaResponse toResponse(Npa npa) {
        if (npa == null) {
            return null;
        }

        NpaResponse response = new NpaResponse();

        response.setNpaId(npa.getNpaId());
        response.setName(npa.getName());
        response.setNumber(npa.getNumber());
        response.setAdoptionDate(npa.getAdoptionDate());
        response.setValidFrom(npa.getValidFrom());
        response.setValidTo(npa.getValidTo());
        response.setOfficialUrl(npa.getOfficialUrl());
        response.setCreatedAt(npa.getCreatedAt());
        response.setUpdatedAt(npa.getUpdatedAt());

        DictionaryValue npaType = npa.getNpaType();

        if (npaType != null) {
            response.setNpaTypeId(npaType.getDictionaryValueId());
            response.setNpaTypeCode(npaType.getCode());
            response.setNpaTypeName(npaType.getLabel());
        }

        DictionaryValue level = npa.getLevel();

        if (level != null) {
            response.setLevelId(level.getDictionaryValueId());
            response.setLevelCode(level.getCode());
            response.setLevelName(level.getLabel());
        }

        DictionaryValue status = npa.getStatus();

        if (status != null) {
            response.setStatusId(status.getDictionaryValueId());
            response.setStatusCode(status.getCode());
            response.setStatusName(status.getLabel());
        }

        return response;
    }
}