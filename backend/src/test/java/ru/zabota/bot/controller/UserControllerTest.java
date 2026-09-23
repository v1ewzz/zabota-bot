package ru.zabota.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Unit-тест UserController.
 *
 * Проверяет:
 *
 * - создание пользователя;
 * - получение пользователя;
 * - получение полного профиля;
 * - обновление пользователя;
 * - удаление пользователя;
 * - валидацию UserRequest.
 *
 * Spring-контекст не запускается.
 * UserService заменён Mockito-моком.
 * MockMvc и ObjectMapper создаются вручную.
 */
class UserControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new UserController(userService)
                )
                .build();
    }

    @Test
    void shouldCreateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        UserRequest request =
                createRequest(
                        regionId,
                        municipalityId
                );

        UserResponse response =
                createUserResponse(
                        userId,
                        regionId,
                        municipalityId
                );

        when(
                userService.create(
                        any(UserRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        post("/api/users")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.userId")
                                .value(
                                        userId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.regionId")
                                .value(
                                        regionId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.municipalityId")
                                .value(
                                        municipalityId.toString()
                                )
                );

        verify(userService)
                .create(
                        any(UserRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {

        UserRequest request =
                new UserRequest();

        request.setRegionId(null);
        request.setMunicipalityId(null);

        mockMvc.perform(
                        post("/api/users")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );

        verify(
                userService,
                never()
        ).create(
                any(UserRequest.class)
        );
    }

    @Test
    void shouldGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        UserResponse response =
                createUserResponse(
                        userId,
                        regionId,
                        municipalityId
                );

        when(userService.getById(userId))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/users/{id}",
                                userId
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.userId")
                                .value(
                                        userId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.regionId")
                                .value(
                                        regionId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.municipalityId")
                                .value(
                                        municipalityId.toString()
                                )
                );

        verify(userService)
                .getById(userId);
    }

    @Test
    void shouldGetUserProfile() throws Exception {
        UUID userId = UUID.randomUUID();

        UserResponse userResponse =
                createUserResponse(
                        userId,
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        UserProfileResponse response =
                new UserProfileResponse();

        response.setUser(userResponse);
        response.setChildren(List.of());

        when(userService.getProfile(userId))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/users/{id}/profile",
                                userId
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.user.userId")
                                .value(
                                        userId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.children.length()")
                                .value(0)
                );

        verify(userService)
                .getProfile(userId);
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID regionId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        UserRequest request =
                createRequest(
                        regionId,
                        municipalityId
                );

        UserResponse response =
                createUserResponse(
                        userId,
                        regionId,
                        municipalityId
                );

        when(
                userService.update(
                        eq(userId),
                        any(UserRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        put(
                                "/api/users/{id}",
                                userId
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.userId")
                                .value(
                                        userId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.regionId")
                                .value(
                                        regionId.toString()
                                )
                );

        verify(userService)
                .update(
                        eq(userId),
                        any(UserRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        UUID userId = UUID.randomUUID();

        UserRequest request =
                new UserRequest();

        request.setRegionId(null);
        request.setMunicipalityId(null);

        mockMvc.perform(
                        put(
                                "/api/users/{id}",
                                userId
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );

        verify(
                userService,
                never()
        ).update(
                eq(userId),
                any(UserRequest.class)
        );
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UUID userId =
                UUID.randomUUID();

        mockMvc.perform(
                        delete(
                                "/api/users/{id}",
                                userId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(userService)
                .delete(userId);
    }

    private UserRequest createRequest(
            UUID regionId,
            UUID municipalityId
    ) {
        UserRequest request =
                new UserRequest();

        request.setRegionId(regionId);
        request.setMunicipalityId(
                municipalityId
        );

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

    private UserResponse createUserResponse(
            UUID userId,
            UUID regionId,
            UUID municipalityId
    ) {
        UserResponse response =
                new UserResponse();

        response.setUserId(userId);

        response.setRegionId(regionId);
        response.setRegionName(
                "Республика Татарстан"
        );

        response.setMunicipalityId(
                municipalityId
        );
        response.setMunicipalityName(
                "Казань"
        );

        response.setPregnancy(false);
        response.setInjury(false);
        response.setDisability(false);
        response.setHousingProblem(false);
        response.setGasificationNeeded(false);

        return response;
    }
}