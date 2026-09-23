package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserChild;

/*
 * Маппер сущности UserChild.
 *
 * Отвечает за ручное преобразование:
 *
 * UserChildRequest → UserChild
 * UserChild → UserChildResponse
 *
 * Связанные User и DictionaryValue передаются
 * из сервисного слоя.
 */
@Component
public class UserChildMapper {

    public UserChild toEntity(
            UserChildRequest request,
            User user,
            DictionaryValue educationLevel,
            DictionaryValue disabilityGroup
    ) {
        if (request == null) {
            return null;
        }

        UserChild userChild = new UserChild();

        updateEntity(
                userChild,
                request,
                user,
                educationLevel,
                disabilityGroup
        );

        return userChild;
    }

    public void updateEntity(
            UserChild userChild,
            UserChildRequest request,
            User user,
            DictionaryValue educationLevel,
            DictionaryValue disabilityGroup
    ) {
        if (userChild == null || request == null) {
            return;
        }

        userChild.setUser(user);
        userChild.setBirthDate(request.getBirthDate());
        userChild.setEducationLevel(educationLevel);
        userChild.setGrade(request.getGrade());
        userChild.setDisability(request.isDisability());
        userChild.setDisabilityGroup(disabilityGroup);
        userChild.setFullTime(request.isFullTime());
    }

    public UserChildResponse toResponse(UserChild userChild) {
        if (userChild == null) {
            return null;
        }

        UserChildResponse response = new UserChildResponse();

        response.setChildId(userChild.getChildId());
        response.setBirthDate(userChild.getBirthDate());
        response.setGrade(userChild.getGrade());
        response.setDisability(userChild.isDisability());
        response.setFullTime(userChild.isFullTime());

        DictionaryValue educationLevel = userChild.getEducationLevel();

        if (educationLevel != null) {
            response.setEducationLevelId(
                    educationLevel.getDictionaryValueId()
            );
            response.setEducationLevelCode(
                    educationLevel.getCode()
            );
            response.setEducationLevelName(
                    educationLevel.getLabel()
            );
        }

        DictionaryValue disabilityGroup = userChild.getDisabilityGroup();

        if (disabilityGroup != null) {
            response.setDisabilityGroupId(
                    disabilityGroup.getDictionaryValueId()
            );
            response.setDisabilityGroupCode(
                    disabilityGroup.getCode()
            );
            response.setDisabilityGroupName(
                    disabilityGroup.getLabel()
            );
        }

        return response;
    }
}