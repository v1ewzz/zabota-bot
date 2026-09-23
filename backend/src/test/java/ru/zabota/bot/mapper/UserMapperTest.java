package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Unit-тест UserMapper.
 *
 * Проверяет преобразования:
 *
 * UserRequest → User
 * User → UserResponse
 * User → UserProfileResponse
 * обновление User данными UserRequest
 *
 * Также проверяется обработка null и корректное
 * преобразование связанных Region, Municipality
 * и DictionaryValue.
 *
 * Spring-контекст не используется.
 */
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldMapRequestToEntity() {
        UserRequest request = createRequest();

        DictionaryValue familyRelation =
                createDictionaryValue(
                        "SPOUSE",
                        "Супруг"
                );

        DictionaryValue militaryStatus =
                createDictionaryValue(
                        "MOBILIZED",
                        "Мобилизован"
                );

        DictionaryValue disabilityGroup =
                createDictionaryValue(
                        "GROUP_2",
                        "II группа"
                );

        DictionaryValue employmentStatus =
                createDictionaryValue(
                        "EMPLOYED",
                        "Работающий"
                );

        DictionaryValue incomeCategory =
                createDictionaryValue(
                        "MIDDLE",
                        "Средний"
                );

        User user = userMapper.toEntity(
                request,
                familyRelation,
                militaryStatus,
                disabilityGroup,
                employmentStatus,
                incomeCategory
        );

        assertEquals(
                request.getPregnancy(),
                user.getPregnancy()
        );

        assertEquals(
                request.isInjury(),
                user.isInjury()
        );

        assertEquals(
                request.isDisability(),
                user.isDisability()
        );

        assertEquals(
                request.isHousingProblem(),
                user.isHousingProblem()
        );

        assertEquals(
                request.isGasificationNeeded(),
                user.isGasificationNeeded()
        );

        assertSame(
                familyRelation,
                user.getFamilyRelation()
        );

        assertSame(
                militaryStatus,
                user.getMilitaryStatus()
        );

        assertSame(
                disabilityGroup,
                user.getDisabilityGroup()
        );

        assertSame(
                employmentStatus,
                user.getEmploymentStatus()
        );

        assertSame(
                incomeCategory,
                user.getIncomeCategory()
        );

        assertNull(user.getRegion());
        assertNull(user.getMunicipality());
    }

    @Test
    void shouldMapRequestToEntityWithoutOptionalDictionaryValues() {
        UserRequest request = createRequest();

        User user = userMapper.toEntity(
                request,
                null,
                null,
                null,
                null,
                null
        );

        assertNull(user.getFamilyRelation());
        assertNull(user.getMilitaryStatus());
        assertNull(user.getDisabilityGroup());
        assertNull(user.getEmploymentStatus());
        assertNull(user.getIncomeCategory());

        assertFalse(user.isInjury());
        assertTrue(user.isDisability());
        assertTrue(user.isHousingProblem());
        assertTrue(user.isGasificationNeeded());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        User result = userMapper.toEntity(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertNull(result);
    }

    @Test
    void shouldUpdateEntity() {
        User user = new User();

        UserRequest request = createRequest();

        Region region = createRegion();
        Municipality municipality =
                createMunicipality();

        DictionaryValue familyRelation =
                createDictionaryValue(
                        "SPOUSE",
                        "Супруг"
                );

        DictionaryValue militaryStatus =
                createDictionaryValue(
                        "MOBILIZED",
                        "Мобилизован"
                );

        DictionaryValue disabilityGroup =
                createDictionaryValue(
                        "GROUP_2",
                        "II группа"
                );

        DictionaryValue employmentStatus =
                createDictionaryValue(
                        "EMPLOYED",
                        "Работающий"
                );

        DictionaryValue incomeCategory =
                createDictionaryValue(
                        "MIDDLE",
                        "Средний"
                );

        userMapper.updateEntity(
                user,
                request,
                region,
                municipality,
                familyRelation,
                militaryStatus,
                disabilityGroup,
                employmentStatus,
                incomeCategory
        );

        assertSame(
                region,
                user.getRegion()
        );

        assertSame(
                municipality,
                user.getMunicipality()
        );

        assertSame(
                familyRelation,
                user.getFamilyRelation()
        );

        assertSame(
                militaryStatus,
                user.getMilitaryStatus()
        );

        assertSame(
                disabilityGroup,
                user.getDisabilityGroup()
        );

        assertSame(
                employmentStatus,
                user.getEmploymentStatus()
        );

        assertSame(
                incomeCategory,
                user.getIncomeCategory()
        );

        assertEquals(
                request.getPregnancy(),
                user.getPregnancy()
        );

        assertEquals(
                request.isInjury(),
                user.isInjury()
        );

        assertEquals(
                request.isDisability(),
                user.isDisability()
        );

        assertEquals(
                request.isHousingProblem(),
                user.isHousingProblem()
        );

        assertEquals(
                request.isGasificationNeeded(),
                user.isGasificationNeeded()
        );
    }

    @Test
    void shouldNotUpdateWhenEntityIsNull() {
        UserRequest request = createRequest();

        userMapper.updateEntity(
                null,
                request,
                createRegion(),
                createMunicipality(),
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void shouldNotUpdateWhenRequestIsNull() {
        User user = new User();

        userMapper.updateEntity(
                user,
                null,
                createRegion(),
                createMunicipality(),
                null,
                null,
                null,
                null,
                null
        );

        assertNull(user.getRegion());
        assertNull(user.getMunicipality());
        assertNull(user.getFamilyRelation());
        assertNull(user.getMilitaryStatus());
    }

    @Test
    void shouldMapEntityToResponse() {
        UUID userId = UUID.randomUUID();

        User user = createUser(userId);

        UserResponse response =
                userMapper.toResponse(user);

        assertEquals(
                userId,
                response.getUserId()
        );

        assertEquals(
                user.getRegion().getRegionId(),
                response.getRegionId()
        );

        assertEquals(
                user.getRegion().getName(),
                response.getRegionName()
        );

        assertEquals(
                user.getMunicipality().getMunicipalityId(),
                response.getMunicipalityId()
        );

        assertEquals(
                user.getMunicipality().getName(),
                response.getMunicipalityName()
        );

        assertEquals(
                user.getFamilyRelation()
                        .getDictionaryValueId(),
                response.getFamilyRelationId()
        );

        assertEquals(
                user.getFamilyRelation().getCode(),
                response.getFamilyRelationCode()
        );

        assertEquals(
                user.getFamilyRelation().getLabel(),
                response.getFamilyRelationName()
        );

        assertEquals(
                user.getMilitaryStatus()
                        .getDictionaryValueId(),
                response.getMilitaryStatusId()
        );

        assertEquals(
                user.getMilitaryStatus().getCode(),
                response.getMilitaryStatusCode()
        );

        assertEquals(
                user.getMilitaryStatus().getLabel(),
                response.getMilitaryStatusName()
        );

        assertEquals(
                user.getPregnancy(),
                response.getPregnancy()
        );

        assertEquals(
                user.isInjury(),
                response.isInjury()
        );

        assertEquals(
                user.isDisability(),
                response.isDisability()
        );

        assertEquals(
                user.getDisabilityGroup()
                        .getDictionaryValueId(),
                response.getDisabilityGroupId()
        );

        assertEquals(
                user.getDisabilityGroup().getCode(),
                response.getDisabilityGroupCode()
        );

        assertEquals(
                user.getDisabilityGroup().getLabel(),
                response.getDisabilityGroupName()
        );

        assertEquals(
                user.isHousingProblem(),
                response.isHousingProblem()
        );

        assertEquals(
                user.isGasificationNeeded(),
                response.isGasificationNeeded()
        );

        assertEquals(
                user.getEmploymentStatus()
                        .getDictionaryValueId(),
                response.getEmploymentStatusId()
        );

        assertEquals(
                user.getEmploymentStatus().getCode(),
                response.getEmploymentStatusCode()
        );

        assertEquals(
                user.getEmploymentStatus().getLabel(),
                response.getEmploymentStatusName()
        );

        assertEquals(
                user.getIncomeCategory()
                        .getDictionaryValueId(),
                response.getIncomeCategoryId()
        );

        assertEquals(
                user.getIncomeCategory().getCode(),
                response.getIncomeCategoryCode()
        );

        assertEquals(
                user.getIncomeCategory().getLabel(),
                response.getIncomeCategoryName()
        );

        assertEquals(
                user.getCreatedAt(),
                response.getCreatedAt()
        );

        assertEquals(
                user.getUpdatedAt(),
                response.getUpdatedAt()
        );
    }

    @Test
    void shouldMapEntityToResponseWithoutRelatedEntities() {
        User user = new User();

        UUID userId = UUID.randomUUID();

        user.setUserId(userId);
        user.setPregnancy(false);
        user.setInjury(false);
        user.setDisability(false);
        user.setHousingProblem(false);
        user.setGasificationNeeded(false);

        UserResponse response =
                userMapper.toResponse(user);

        assertEquals(
                userId,
                response.getUserId()
        );

        assertNull(response.getRegionId());
        assertNull(response.getRegionName());

        assertNull(response.getMunicipalityId());
        assertNull(response.getMunicipalityName());

        assertNull(response.getFamilyRelationId());
        assertNull(response.getFamilyRelationCode());
        assertNull(response.getFamilyRelationName());

        assertNull(response.getMilitaryStatusId());
        assertNull(response.getMilitaryStatusCode());
        assertNull(response.getMilitaryStatusName());

        assertNull(response.getDisabilityGroupId());
        assertNull(response.getDisabilityGroupCode());
        assertNull(response.getDisabilityGroupName());

        assertNull(response.getEmploymentStatusId());
        assertNull(response.getEmploymentStatusCode());
        assertNull(response.getEmploymentStatusName());

        assertFalse(response.isInjury());
        assertFalse(response.isDisability());
        assertFalse(response.isHousingProblem());
        assertFalse(response.isGasificationNeeded());
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        assertNull(
                userMapper.toResponse(null)
        );
    }

    @Test
    void shouldMapUserToProfileResponse() {
        UUID userId = UUID.randomUUID();

        User user = createUser(userId);

        UserResponse userResponse =
                userMapper.toResponse(user);

        UserChildResponse firstChild =
                new UserChildResponse();

        firstChild.setChildId(
                UUID.randomUUID()
        );

        UserChildResponse secondChild =
                new UserChildResponse();

        secondChild.setChildId(
                UUID.randomUUID()
        );

        List<UserChildResponse> children =
                List.of(
                        firstChild,
                        secondChild
                );

        UserProfileResponse response =
                userMapper.toProfileResponse(
                        user,
                        children
                );

        assertEquals(
                userResponse.getUserId(),
                response.getUser().getUserId()
        );

        assertSame(
                children,
                response.getChildren()
        );

        assertEquals(
                2,
                response.getChildren().size()
        );
    }

    @Test
    void shouldReturnNullWhenUserIsNullForProfile() {
        List<UserChildResponse> children =
                List.of();

        assertNull(
                userMapper.toProfileResponse(
                        null,
                        children
                )
        );
    }

    private UserRequest createRequest() {
        UserRequest request =
                new UserRequest();

        request.setRegionId(
                UUID.randomUUID()
        );

        request.setMunicipalityId(
                UUID.randomUUID()
        );

        request.setFamilyRelationId(
                UUID.randomUUID()
        );

        request.setMilitaryStatusId(
                UUID.randomUUID()
        );

        request.setPregnancy(true);
        request.setInjury(false);
        request.setDisability(true);

        request.setDisabilityGroupId(
                UUID.randomUUID()
        );

        request.setHousingProblem(true);
        request.setGasificationNeeded(true);

        request.setEmploymentStatusId(
                UUID.randomUUID()
        );

        request.setIncomeCategoryId(
                UUID.randomUUID()
        );

        return request;
    }

    private User createUser(UUID userId) {
        User user = new User();

        user.setUserId(userId);

        Region region = createRegion();
        Municipality municipality =
                createMunicipality();

        DictionaryValue familyRelation =
                createDictionaryValue(
                        "SPOUSE",
                        "Супруг"
                );

        DictionaryValue militaryStatus =
                createDictionaryValue(
                        "MOBILIZED",
                        "Мобилизован"
                );

        DictionaryValue disabilityGroup =
                createDictionaryValue(
                        "GROUP_2",
                        "II группа"
                );

        DictionaryValue employmentStatus =
                createDictionaryValue(
                        "EMPLOYED",
                        "Работающий"
                );

        DictionaryValue incomeCategory =
                createDictionaryValue(
                        "MIDDLE",
                        "Средний"
                );

        user.setRegion(region);
        user.setMunicipality(municipality);
        user.setFamilyRelation(familyRelation);
        user.setMilitaryStatus(militaryStatus);

        user.setPregnancy(true);
        user.setInjury(false);
        user.setDisability(true);
        user.setDisabilityGroup(disabilityGroup);
        user.setHousingProblem(true);
        user.setGasificationNeeded(true);

        user.setEmploymentStatus(
                employmentStatus
        );

        user.setIncomeCategory(
                incomeCategory
        );

        return user;
    }

    private Region createRegion() {
        Region region = new Region();

        region.setRegionId(
                UUID.randomUUID()
        );

        region.setName(
                "Республика Татарстан"
        );

        region.setCode("16");

        return region;
    }

    private Municipality createMunicipality() {
        Municipality municipality =
                new Municipality();

        municipality.setMunicipalityId(
                UUID.randomUUID()
        );

        municipality.setName("Казань");

        return municipality;
    }

    private DictionaryValue createDictionaryValue(
            String code,
            String label
    ) {
        DictionaryValue value =
                new DictionaryValue();

        value.setDictionaryValueId(
                UUID.randomUUID()
        );

        value.setCode(code);
        value.setLabel(label);

        return value;
    }
}