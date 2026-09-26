package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserChild;

/*
 * Маппер данных ребёнка пользователя.
 */
@Component
public class UserChildMapper {

    public UserChild toEntity(
            UserChildRequest request,
            User user,
            DictionaryValue educationLevel,
            DictionaryValue disabilityGroup
    ) {
        return toEntity(
                request,
                user,
                educationLevel,
                disabilityGroup,
                null
        );
    }

    public UserChild toEntity(
            UserChildRequest request,
            User user,
            DictionaryValue educationLevel,
            DictionaryValue disabilityGroup,
            DictionaryValue institutionType
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
                disabilityGroup,
                institutionType
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
        updateEntity(
                userChild,
                request,
                user,
                educationLevel,
                disabilityGroup,
                null
        );
    }

    public void updateEntity(
            UserChild userChild,
            UserChildRequest request,
            User user,
            DictionaryValue educationLevel,
            DictionaryValue disabilityGroup,
            DictionaryValue institutionType
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
        userChild.setInstitutionType(institutionType);
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
        setDictionaryResponse(
                userChild.getEducationLevel(),
                response::setEducationLevelId,
                response::setEducationLevelCode,
                response::setEducationLevelName
        );
        setDictionaryResponse(
                userChild.getDisabilityGroup(),
                response::setDisabilityGroupId,
                response::setDisabilityGroupCode,
                response::setDisabilityGroupName
        );
        setDictionaryResponse(
                userChild.getInstitutionType(),
                response::setInstitutionTypeId,
                response::setInstitutionTypeCode,
                response::setInstitutionTypeName
        );
        return response;
    }

    private void setDictionaryResponse(
            DictionaryValue value,
            IdSetter idSetter,
            StringSetter codeSetter,
            StringSetter nameSetter
    ) {
        if (value == null) {
            return;
        }
        idSetter.set(value.getDictionaryValueId());
        codeSetter.set(value.getCode());
        nameSetter.set(value.getLabel());
    }

    @FunctionalInterface
    private interface IdSetter {
        void set(java.util.UUID id);
    }

    @FunctionalInterface
    private interface StringSetter {
        void set(String value);
    }
}
