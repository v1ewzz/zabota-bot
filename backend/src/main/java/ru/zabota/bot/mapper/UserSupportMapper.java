package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.UserSupport;

/*
 * Маппер персонального результата подбора.
 *
 * Преобразует UserSupport вместе с доступными данными
 * SupportMeasure и DictionaryValue в безопасный DTO для клиента.
 * Внутренние support_rule и NPA в ответ не попадают.
 */
@Component
public class UserSupportMapper {

    public UserSupportResponse toResponse(UserSupport userSupport) {
        if (userSupport == null) {
            return null;
        }

        UserSupportResponse response = new UserSupportResponse();

        response.setUserSupportId(userSupport.getUserSupportId());

        if (userSupport.getSupport() != null) {
            var support = userSupport.getSupport();

            response.setSupportId(support.getSupportId());
            response.setName(support.getName());
            response.setDescription(support.getDescription());
            response.setAmount(
                    userSupport.getMatchedAmount() != null
                            ? userSupport.getMatchedAmount()
                            : support.getAmount()
            );
            response.setApplicationRequired(support.isApplicationRequired());
            response.setActionUrl(support.getActionUrl());

            DictionaryValue frequency = support.getFrequency();
            if (frequency != null) {
                response.setFrequency(frequency.getLabel());
            }

            DictionaryValue applicationChannel = support.getApplicationChannel();
            if (applicationChannel != null) {
                response.setApplicationChannel(applicationChannel.getLabel());
            }
        }

        DictionaryValue status = userSupport.getStatus();
        if (status != null) {
            response.setStatusId(status.getDictionaryValueId());
            response.setStatusCode(status.getCode());
            response.setStatusName(status.getLabel());
        }

        response.setSelectedForAction(userSupport.isSelectedForAction());
        response.setCheckedAt(userSupport.getCheckedAt());
        response.setSubmittedAt(userSupport.getSubmittedAt());
        response.setReceivedAt(userSupport.getReceivedAt());
        response.setNote(userSupport.getNote());

        return response;
    }
}
