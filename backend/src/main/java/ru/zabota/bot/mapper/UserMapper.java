package ru.zabota.bot.mapper;

import org.springframework.stereotype.Component;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/*
 * Маппер User.
 *
 * Преобразует DTO анкеты в Entity и Entity в DTO профиля.
 * Поиск связанных объектов и бизнес-правила остаются в сервисах.
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
        return toEntity(
                request,
                familyRelation,
                militaryStatus,
                null,
                disabilityGroup,
                employmentStatus,
                incomeCategory,
                null
        );
    }

    public User toEntity(
            UserRequest request,
            DictionaryValue familyRelation,
            DictionaryValue militaryStatus,
            DictionaryValue sex,
            DictionaryValue disabilityGroup,
            DictionaryValue employmentStatus,
            DictionaryValue incomeCategory,
            DictionaryValue legalIssueCategory
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
                sex,
                disabilityGroup,
                employmentStatus,
                incomeCategory,
                legalIssueCategory
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
        updateEntity(
                user,
                request,
                region,
                municipality,
                familyRelation,
                militaryStatus,
                null,
                disabilityGroup,
                employmentStatus,
                incomeCategory,
                null
        );
    }

    public void updateEntity(
            User user,
            UserRequest request,
            Region region,
            Municipality municipality,
            DictionaryValue familyRelation,
            DictionaryValue militaryStatus,
            DictionaryValue sex,
            DictionaryValue disabilityGroup,
            DictionaryValue employmentStatus,
            DictionaryValue incomeCategory,
            DictionaryValue legalIssueCategory
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
        user.setBirthDate(request.getBirthDate());
        user.setPregnancy(request.getPregnancy());
        user.setPregnancyDays(request.getPregnancyDays());
        user.setSex(sex);
        user.setInjury(request.isInjury());
        user.setDisability(request.isDisability());
        user.setDisabilityGroup(disabilityGroup);
        user.setHousingProblem(request.isHousingProblem());
        user.setGasificationNeeded(request.isGasificationNeeded());
        user.setEmploymentStatus(employmentStatus);
        user.setIncomeCategory(incomeCategory);
        user.setLoanExists(request.isLoanExists());
        user.setBusinessPlan(request.isBusinessPlan());
        user.setJobSeeker(request.isJobSeeker());
        user.setSocialServiceNeed(request.isSocialServiceNeed());
        user.setServicemanLeaveStart(request.getServicemanLeaveStart());
        user.setServicemanLeaveEnd(request.getServicemanLeaveEnd());
        user.setLegalIssueCategory(legalIssueCategory);
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

        Municipality municipality = user.getMunicipality();
        if (municipality != null) {
            response.setMunicipalityId(municipality.getMunicipalityId());
            response.setMunicipalityName(municipality.getName());
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
        setDictionaryResponse(
                user.getSex(),
                response::setSexId,
                response::setSexCode,
                response::setSexName
        );
        setDictionaryResponse(
                user.getDisabilityGroup(),
                response::setDisabilityGroupId,
                response::setDisabilityGroupCode,
                response::setDisabilityGroupName
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
        setDictionaryResponse(
                user.getLegalIssueCategory(),
                response::setLegalIssueCategoryId,
                response::setLegalIssueCategoryCode,
                response::setLegalIssueCategoryName
        );

        response.setBirthDate(user.getBirthDate());
        response.setAge(calculateAge(user.getBirthDate(), LocalDate.now()));
        response.setPregnancy(user.getPregnancy());
        response.setPregnancyDays(user.getPregnancyDays());
        response.setInjury(user.isInjury());
        response.setDisability(user.isDisability());
        response.setHousingProblem(user.isHousingProblem());
        response.setGasificationNeeded(user.isGasificationNeeded());
        response.setLoanExists(user.isLoanExists());
        response.setBusinessPlan(user.isBusinessPlan());
        response.setJobSeeker(user.isJobSeeker());
        response.setSocialServiceNeed(user.isSocialServiceNeed());
        response.setServicemanLeaveStart(user.getServicemanLeaveStart());
        response.setServicemanLeaveEnd(user.getServicemanLeaveEnd());
        response.setServicemanOnLeave(isDateInRange(
                user.getServicemanLeaveStart(),
                user.getServicemanLeaveEnd(),
                LocalDate.now()
        ));
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public UserProfileResponse toProfileResponse(
            User user,
            List<ru.zabota.bot.dto.user.UserChildResponse> children
    ) {
        if (user == null) {
            return null;
        }

        UserProfileResponse response = new UserProfileResponse();
        response.setUser(toResponse(user));
        response.setChildren(children);
        return response;
    }

    private int calculateAge(LocalDate birthDate, LocalDate today) {
        if (birthDate == null || birthDate.isAfter(today)) {
            return 0;
        }
        return Period.between(birthDate, today).getYears();
    }

    private boolean isDateInRange(LocalDate start, LocalDate end, LocalDate date) {
        if (start == null || end == null) {
            return false;
        }
        return !date.isBefore(start) && !date.isAfter(end);
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
