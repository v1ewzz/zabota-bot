package ru.zabota.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserChild;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.UserChildMapper;
import ru.zabota.bot.mapper.UserMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.MunicipalityRepository;
import ru.zabota.bot.repository.RegionRepository;
import ru.zabota.bot.repository.UserChildRepository;
import ru.zabota.bot.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit-тест UserService.
 *
 * Проверяет:
 *
 * - создание пользователя;
 * - создание пользователя с детьми;
 * - получение пользователя;
 * - получение полного профиля;
 * - обновление пользователя;
 * - обновление списка детей;
 * - удаление пользователя;
 * - обработку отсутствующего пользователя;
 * - обработку отсутствующих региона, муниципалитета
 *   и справочных значений.
 *
 * Spring-контекст и база данных не используются.
 * Все внешние зависимости заменяются Mockito-моками.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserChildRepository userChildRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private MunicipalityRepository municipalityRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserChildMapper userChildMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserWithoutChildren() {
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        UserRequest request = createUserRequest(
                regionId,
                municipalityId
        );

        Region region = createRegion(regionId);
        Municipality municipality =
                createMunicipality(municipalityId);

        User user = new User();

        UserResponse expected =
                createUserResponse(userId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.of(municipality));

        when(userMapper.toEntity(
                request,
                null,
                null,
                null,
                null,
                null
        )).thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(expected);

        UserResponse result =
                userService.create(request);

        assertEquals(expected, result);

        verify(regionRepository)
                .findById(regionId);

        verify(municipalityRepository)
                .findById(municipalityId);

        verify(userMapper).toEntity(
                request,
                null,
                null,
                null,
                null,
                null
        );

        verify(userRepository)
                .save(user);

        verify(userChildRepository, never())
                .saveAll(anyList());

        verify(userMapper)
                .toResponse(user);
    }

    @Test
    void shouldCreateUserWithChildren() {
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();
        UUID educationLevelId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        regionId,
                        municipalityId
                );

        UserChildRequest childRequest =
                createChildRequest(
                        educationLevelId
                );

        request.setChildren(
                List.of(childRequest)
        );

        Region region = createRegion(regionId);

        Municipality municipality =
                createMunicipality(municipalityId);

        DictionaryValue educationLevel =
                createDictionaryValue(
                        educationLevelId,
                        "SCHOOL",
                        "Школа"
                );

        User user = new User();

        UserChild child =
                new UserChild();

        UserResponse expected =
                createUserResponse(userId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.of(municipality));

        when(dictionaryValueRepository.findById(
                educationLevelId
        )).thenReturn(
                Optional.of(educationLevel)
        );

        when(userMapper.toEntity(
                request,
                null,
                null,
                null,
                null,
                null
        )).thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(user);

        when(userChildMapper.toEntity(
                childRequest,
                user,
                educationLevel,
                null
        )).thenReturn(child);

        when(userMapper.toResponse(user))
                .thenReturn(expected);

        UserResponse result =
                userService.create(request);

        assertEquals(expected, result);

        verify(regionRepository)
                .findById(regionId);

        verify(municipalityRepository)
                .findById(municipalityId);

        verify(dictionaryValueRepository)
                .findById(educationLevelId);

        verify(userMapper).toEntity(
                request,
                null,
                null,
                null,
                null,
                null
        );

        verify(userRepository)
                .save(user);

        verify(userChildMapper).toEntity(
                childRequest,
                user,
                educationLevel,
                null
        );

        verify(userChildRepository)
                .saveAll(List.of(child));

        verify(userMapper)
                .toResponse(user);
    }

    @Test
    void shouldThrowExceptionWhenRegionNotFoundOnCreate() {
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        regionId,
                        municipalityId
                );

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.create(request)
        );

        verify(regionRepository)
                .findById(regionId);

        verify(municipalityRepository, never())
                .findById(municipalityId);

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenMunicipalityNotFoundOnCreate() {
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        regionId,
                        municipalityId
                );

        Region region =
                createRegion(regionId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.create(request)
        );

        verify(regionRepository)
                .findById(regionId);

        verify(municipalityRepository)
                .findById(municipalityId);

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenDictionaryValueNotFoundOnCreate() {
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();
        UUID militaryStatusId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        regionId,
                        municipalityId
                );

        request.setMilitaryStatusId(
                militaryStatusId
        );

        Region region =
                createRegion(regionId);

        Municipality municipality =
                createMunicipality(municipalityId);

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.of(municipality));

        when(dictionaryValueRepository.findById(
                militaryStatusId
        )).thenReturn(
                Optional.empty()
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.create(request)
        );

        verify(dictionaryValueRepository)
                .findById(militaryStatusId);

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldGetUserById() {
        UUID userId = UUID.randomUUID();

        User user = new User();

        UserResponse expected =
                createUserResponse(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userMapper.toResponse(user))
                .thenReturn(expected);

        UserResponse result =
                userService.getById(userId);

        assertEquals(expected, result);

        verify(userRepository)
                .findById(userId);

        verify(userMapper)
                .toResponse(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetById() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getById(userId)
        );

        verify(userRepository)
                .findById(userId);

        verify(userMapper, never())
                .toResponse(any());
    }

    @Test
    void shouldGetUserProfileWithChildren() {
        UUID userId = UUID.randomUUID();

        User user = new User();

        UserChild firstChild =
                new UserChild();

        UserChild secondChild =
                new UserChild();

        UserChildResponse firstResponse =
                createChildResponse(
                        UUID.randomUUID()
                );

        UserChildResponse secondResponse =
                createChildResponse(
                        UUID.randomUUID()
                );

        List<UserChildResponse> children =
                List.of(
                        firstResponse,
                        secondResponse
                );

        UserResponse userResponse =
                createUserResponse(userId);

        UserProfileResponse expected =
                new UserProfileResponse();

        expected.setUser(userResponse);
        expected.setChildren(children);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userChildRepository
                .findAllByUser_UserId(userId))
                .thenReturn(
                        List.of(
                                firstChild,
                                secondChild
                        )
                );

        when(userChildMapper.toResponse(firstChild))
                .thenReturn(firstResponse);

        when(userChildMapper.toResponse(secondChild))
                .thenReturn(secondResponse);

        when(userMapper.toProfileResponse(
                user,
                children
        )).thenReturn(expected);

        UserProfileResponse result =
                userService.getProfile(userId);

        assertEquals(expected, result);

        verify(userRepository)
                .findById(userId);

        verify(userChildRepository)
                .findAllByUser_UserId(userId);

        verify(userChildMapper)
                .toResponse(firstChild);

        verify(userChildMapper)
                .toResponse(secondChild);

        verify(userMapper)
                .toProfileResponse(
                        user,
                        children
                );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetProfile() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getProfile(userId)
        );

        verify(userRepository)
                .findById(userId);

        verify(userChildRepository, never())
                .findAllByUser_UserId(userId);
    }

    @Test
    void shouldUpdateUserWithoutChildren() {
        UUID userId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        regionId,
                        municipalityId
                );

        User user = new User();

        Region region =
                createRegion(regionId);

        Municipality municipality =
                createMunicipality(municipalityId);

        UserResponse expected =
                createUserResponse(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.of(municipality));

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(expected);

        UserResponse result =
                userService.update(
                        userId,
                        request
                );

        assertEquals(expected, result);

        verify(userRepository)
                .findById(userId);

        verify(regionRepository)
                .findById(regionId);

        verify(municipalityRepository)
                .findById(municipalityId);

        verify(userMapper).updateEntity(
                user,
                request,
                region,
                municipality,
                null,
                null,
                null,
                null,
                null
        );

        verify(userRepository)
                .save(user);

        verify(userChildRepository)
                .deleteAllByUser_UserId(userId);

        verify(userChildRepository, never())
                .saveAll(anyList());

        verify(userMapper)
                .toResponse(user);
    }

    @Test
    void shouldUpdateUserWithChildren() {
        UUID userId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();
        UUID educationLevelId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        regionId,
                        municipalityId
                );

        UserChildRequest childRequest =
                createChildRequest(
                        educationLevelId
                );

        request.setChildren(
                List.of(childRequest)
        );

        User user = new User();

        Region region =
                createRegion(regionId);

        Municipality municipality =
                createMunicipality(municipalityId);

        DictionaryValue educationLevel =
                createDictionaryValue(
                        educationLevelId,
                        "SCHOOL",
                        "Школа"
                );

        UserChild child =
                new UserChild();

        UserResponse expected =
                createUserResponse(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.of(municipality));

        when(dictionaryValueRepository.findById(
                educationLevelId
        )).thenReturn(
                Optional.of(educationLevel)
        );

        when(userRepository.save(user))
                .thenReturn(user);

        when(userChildMapper.toEntity(
                childRequest,
                user,
                educationLevel,
                null
        )).thenReturn(child);

        when(userMapper.toResponse(user))
                .thenReturn(expected);

        UserResponse result =
                userService.update(
                        userId,
                        request
                );

        assertEquals(expected, result);

        verify(userMapper).updateEntity(
                user,
                request,
                region,
                municipality,
                null,
                null,
                null,
                null,
                null
        );

        verify(userRepository)
                .save(user);

        verify(userChildRepository)
                .deleteAllByUser_UserId(userId);

        verify(userChildMapper).toEntity(
                childRequest,
                user,
                educationLevel,
                null
        );

        verify(userChildRepository)
                .saveAll(List.of(child));

        verify(userMapper)
                .toResponse(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        UUID userId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.update(
                        userId,
                        request
                )
        );

        verify(userRepository)
                .findById(userId);

        verify(userRepository, never())
                .save(any());

        verify(userMapper, never())
                .updateEntity(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                );
    }

    @Test
    void shouldDeleteUser() {
        UUID userId = UUID.randomUUID();

        User user = new User();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository)
                .findById(userId);

        verify(userRepository)
                .delete(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDelete() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(userId)
        );

        verify(userRepository)
                .findById(userId);

        verify(userRepository, never())
                .delete(any());
    }

    @Test
    void shouldThrowExceptionWhenChildEducationLevelNotFound() {
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();
        UUID educationLevelId = UUID.randomUUID();

        UserRequest request =
                createUserRequest(
                        regionId,
                        municipalityId
                );

        request.setChildren(
                List.of(
                        createChildRequest(
                                educationLevelId
                        )
                )
        );

        Region region =
                createRegion(regionId);

        Municipality municipality =
                createMunicipality(municipalityId);

        User user = new User();

        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(municipalityRepository.findById(municipalityId))
                .thenReturn(Optional.of(municipality));

        when(userMapper.toEntity(
                request,
                null,
                null,
                null,
                null,
                null
        )).thenReturn(user);

        when(userRepository.save(user))
                .thenReturn(user);

        when(dictionaryValueRepository.findById(
                educationLevelId
        )).thenReturn(
                Optional.empty()
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.create(request)
        );

        verify(userChildRepository, never())
                .saveAll(anyList());
    }

    private UserRequest createUserRequest(
            UUID regionId,
            UUID municipalityId
    ) {
        UserRequest request =
                new UserRequest();

        request.setFirstName("Иван");
        request.setLastName("Иванов");

        request.setRegionId(regionId);
        request.setMunicipalityId(municipalityId);

        request.setFamilyRelationId(null);
        request.setMilitaryStatusId(null);
        request.setPregnancy(false);
        request.setInjury(false);
        request.setDisability(false);
        request.setDisabilityGroupId(null);
        request.setHousingProblem(false);
        request.setGasificationNeeded(false);
        request.setEmploymentStatusId(null);
        request.setIncomeCategoryId(null);
        request.setChildren(List.of());

        return request;
    }

    private UserChildRequest createChildRequest(
            UUID educationLevelId
    ) {
        UserChildRequest request =
                new UserChildRequest();

        request.setBirthDate(
                LocalDate.of(2017, 5, 10)
        );

        request.setEducationLevelId(
                educationLevelId
        );

        request.setGrade((short) 3);
        request.setDisability(false);
        request.setDisabilityGroupId(null);
        request.setFullTime(true);

        return request;
    }

    private Region createRegion(UUID id) {
        Region region = new Region();

        region.setRegionId(id);
        region.setName("Республика Татарстан");
        region.setCode("16");

        return region;
    }

    private Municipality createMunicipality(UUID id) {
        Municipality municipality =
                new Municipality();

        municipality.setMunicipalityId(id);
        municipality.setName("Казань");

        return municipality;
    }

    private DictionaryValue createDictionaryValue(
            UUID id,
            String code,
            String label
    ) {
        DictionaryValue value =
                new DictionaryValue();

        value.setDictionaryValueId(id);
        value.setCode(code);
        value.setLabel(label);

        return value;
    }

    private UserResponse createUserResponse(
            UUID id
    ) {
        UserResponse response =
                new UserResponse();

        response.setUserId(id);

        return response;
    }

    private UserChildResponse createChildResponse(
            UUID id
    ) {
        UserChildResponse response =
                new UserChildResponse();

        response.setChildId(id);

        return response;
    }
}