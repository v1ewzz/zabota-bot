package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.support.NpaShortResponse;
import ru.zabota.bot.dto.support.SupportMeasureRequest;
import ru.zabota.bot.dto.support.SupportMeasureResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Npa;
import ru.zabota.bot.entity.SupportMeasure;

import java.util.List;

/*
 * Маппер SupportMeasure.
 *
 * Преобразует меру и связанные с ней справочные значения
 * в DTO. Список НПА передаётся сервисным слоем.
 */
@Component
public class SupportMeasureMapper {

    public SupportMeasure toEntity(
            SupportMeasureRequest request,
            DictionaryValue supportType,
            DictionaryValue level,
            DictionaryValue recipientType,
            DictionaryValue applicationChannel,
            DictionaryValue frequency,
            DictionaryValue verificationStatus
    ) {
        if (request == null) {
            return null;
        }

        SupportMeasure supportMeasure = new SupportMeasure();
        updateEntity(
                supportMeasure,
                request,
                supportType,
                level,
                recipientType,
                applicationChannel,
                frequency,
                verificationStatus
        );
        return supportMeasure;
    }

    public void updateEntity(
            SupportMeasure supportMeasure,
            SupportMeasureRequest request,
            DictionaryValue supportType,
            DictionaryValue level,
            DictionaryValue recipientType,
            DictionaryValue applicationChannel,
            DictionaryValue frequency,
            DictionaryValue verificationStatus
    ) {
        if (supportMeasure == null || request == null) {
            return;
        }

        supportMeasure.setName(request.getName());
        supportMeasure.setDescription(request.getDescription());
        supportMeasure.setSupportType(supportType);
        supportMeasure.setLevel(level);
        supportMeasure.setRecipientType(recipientType);
        supportMeasure.setApplicationRequired(request.getApplicationRequired());
        supportMeasure.setApplicationChannel(applicationChannel);
        supportMeasure.setAmount(request.getAmount());
        supportMeasure.setFrequency(frequency);
        supportMeasure.setDocuments(request.getDocuments());
        supportMeasure.setValidFrom(request.getValidFrom());
        supportMeasure.setValidTo(request.getValidTo());
        supportMeasure.setVerificationStatus(verificationStatus);
        supportMeasure.setActionUrl(request.getActionUrl());
    }

    public SupportMeasureResponse toResponse(SupportMeasure supportMeasure) {
        return toResponse(supportMeasure, List.of());
    }

    public SupportMeasureResponse toResponse(
            SupportMeasure supportMeasure,
            List<Npa> npas
    ) {
        if (supportMeasure == null) {
            return null;
        }

        SupportMeasureResponse response = new SupportMeasureResponse();
        response.setSupportId(supportMeasure.getSupportId());
        response.setName(supportMeasure.getName());
        response.setDescription(supportMeasure.getDescription());
        response.setApplicationRequired(supportMeasure.isApplicationRequired());
        response.setAmount(supportMeasure.getAmount());
        response.setDocuments(supportMeasure.getDocuments());
        response.setValidFrom(supportMeasure.getValidFrom());
        response.setValidTo(supportMeasure.getValidTo());
        response.setActionUrl(supportMeasure.getActionUrl());
        response.setNpa(npas.stream().map(this::toNpaResponse).toList());
        response.setCreatedAt(supportMeasure.getCreatedAt());
        response.setUpdatedAt(supportMeasure.getUpdatedAt());

        mapSupportType(supportMeasure.getSupportType(), response);
        mapLevel(supportMeasure.getLevel(), response);
        mapRecipientType(supportMeasure.getRecipientType(), response);
        mapApplicationChannel(supportMeasure.getApplicationChannel(), response);
        mapFrequency(supportMeasure.getFrequency(), response);
        mapVerificationStatus(supportMeasure.getVerificationStatus(), response);
        return response;
    }

    private NpaShortResponse toNpaResponse(Npa npa) {
        NpaShortResponse response = new NpaShortResponse();
        response.setNpaId(npa.getNpaId());
        response.setName(npa.getName());
        response.setNumber(npa.getNumber());
        response.setAdoptionDate(npa.getAdoptionDate());
        response.setOfficialUrl(npa.getOfficialUrl());
        return response;
    }

    private void mapSupportType(DictionaryValue value, SupportMeasureResponse response) {
        if (value == null) return;
        response.setSupportTypeId(value.getDictionaryValueId());
        response.setSupportTypeCode(value.getCode());
        response.setSupportTypeName(value.getLabel());
    }

    private void mapLevel(DictionaryValue value, SupportMeasureResponse response) {
        if (value == null) return;
        response.setLevelId(value.getDictionaryValueId());
        response.setLevelCode(value.getCode());
        response.setLevelName(value.getLabel());
    }

    private void mapRecipientType(DictionaryValue value, SupportMeasureResponse response) {
        if (value == null) return;
        response.setRecipientTypeId(value.getDictionaryValueId());
        response.setRecipientTypeCode(value.getCode());
        response.setRecipientTypeName(value.getLabel());
    }

    private void mapApplicationChannel(DictionaryValue value, SupportMeasureResponse response) {
        if (value == null) return;
        response.setApplicationChannelId(value.getDictionaryValueId());
        response.setApplicationChannelCode(value.getCode());
        response.setApplicationChannelName(value.getLabel());
    }

    private void mapFrequency(DictionaryValue value, SupportMeasureResponse response) {
        if (value == null) return;
        response.setFrequencyId(value.getDictionaryValueId());
        response.setFrequencyCode(value.getCode());
        response.setFrequencyName(value.getLabel());
    }

    private void mapVerificationStatus(DictionaryValue value, SupportMeasureResponse response) {
        if (value == null) return;
        response.setVerificationStatusId(value.getDictionaryValueId());
        response.setVerificationStatusCode(value.getCode());
        response.setVerificationStatusName(value.getLabel());
    }
}
