package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.Municipality;

import java.util.List;

/*
 * Маппер сущности User.
 *
 * Отвечает за преобразование:
 *
 * UserRequest → User
 * User → UserResponse
 * User → UserProfileResponse
 *
 * Связанные сущности Region, Municipality и DictionaryValue
 * передаются из сервисного слоя.
 *
 * Маппер не выполняет поиск связанных объектов в базе данных
 * и не содержит бизнес-логики.
 */
@Component
public class UserMapper {

    public User toEntity(
            UserRequest request,
            DictionaryValue familyRelation,
            DictionaryValue militaryStatus,
            DictionaryValue disabilityGroup,
            DictionaryValue employmentStatus,
            DictionaryValue incomeCategory
    ) {
        if (request == null) {
            return null;
        }

        User user = new User();

        updateEntity(
                user,
                request,
                null,
                null,
                familyRelation,
                militaryStatus,
                disabilityGroup,
                employmentStatus,
                incomeCategory
        );

        return user;
    }

    public void updateEntity(
            User user,
            UserRequest request,
            Region region,
            Municipality municipality,
            DictionaryValue familyRelation,
            DictionaryValue militaryStatus,
            DictionaryValue disabilityGroup,
            DictionaryValue employmentStatus,
            DictionaryValue incomeCategory
    ) {
        if (user == null || request == null) {
            return;
        }

        user.setFirstName(trimToNull(request.getFirstName()));
        user.setLastName(trimToNull(request.getLastName()));

        user.setRegion(region);
        user.setMunicipality(municipality);
        user.setFamilyRelation(familyRelation);
        user.setMilitaryStatus(militaryStatus);

        user.setPregnancy(request.getPregnancy());
        user.setInjury(request.isInjury());
        user.setDisability(request.isDisability());
        user.setDisabilityGroup(disabilityGroup);
        user.setHousingProblem(request.isHousingProblem());
        user.setGasificationNeeded(
                request.isGasificationNeeded()
        );

        user.setEmploymentStatus(employmentStatus);
        user.setIncomeCategory(incomeCategory);
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();

        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());

        Region region = user.getRegion();

        if (region != null) {
            response.setRegionId(region.getRegionId());
            response.setRegionName(region.getName());
        }

        Municipality municipality =
                user.getMunicipality();

        if (municipality != null) {
            response.setMunicipalityId(
                    municipality.getMunicipalityId()
            );
            response.setMunicipalityName(
                    municipality.getName()
            );
        }

        setDictionaryResponse(
                user.getFamilyRelation(),
                response::setFamilyRelationId,
                response::setFamilyRelationCode,
                response::setFamilyRelationName
        );

        setDictionaryResponse(
                user.getMilitaryStatus(),
                response::setMilitaryStatusId,
                response::setMilitaryStatusCode,
                response::setMilitaryStatusName
        );

        response.setPregnancy(user.getPregnancy());
        response.setInjury(user.isInjury());
        response.setDisability(user.isDisability());

        setDictionaryResponse(
                user.getDisabilityGroup(),
                response::setDisabilityGroupId,
                response::setDisabilityGroupCode,
                response::setDisabilityGroupName
        );

        response.setHousingProblem(
                user.isHousingProblem()
        );

        response.setGasificationNeeded(
                user.isGasificationNeeded()
        );

        setDictionaryResponse(
                user.getEmploymentStatus(),
                response::setEmploymentStatusId,
                response::setEmploymentStatusCode,
                response::setEmploymentStatusName
        );

        setDictionaryResponse(
                user.getIncomeCategory(),
                response::setIncomeCategoryId,
                response::setIncomeCategoryCode,
                response::setIncomeCategoryName
        );

        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }

    public UserProfileResponse toProfileResponse(
            User user,
            List<UserChildResponse> children
    ) {
        if (user == null) {
            return null;
        }

        UserProfileResponse response =
                new UserProfileResponse();

        response.setUser(toResponse(user));
        response.setChildren(children);

        return response;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
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