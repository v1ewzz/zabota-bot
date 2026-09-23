package ru.zabota.bot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;
import ru.zabota.bot.service.UserSupportService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Unit-тест UserSupportController.
 *
 * Проверяет HTTP-контракт личного кабинета:
 * подбор, получение списка и изменение персональной меры.
 */
@WebMvcTest(UserSupportController.class)
class UserSupportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserSupportService userSupportService;

    @Test
    void rematch_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        SupportSearchResponse response = new SupportSearchResponse();
        response.setUserId(userId);
        response.setMeasures(List.of());

        when(userSupportService.rematch(userId))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/users/{userId}/supports/search", userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.measures").isArray());

        verify(userSupportService).rematch(userId);
    }

    @Test
    void getAll_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        UserSupportResponse response = new UserSupportResponse();
        response.setUserSupportId(UUID.randomUUID());

        when(userSupportService.getAll(userId))
                .thenReturn(List.of(response));

        mockMvc.perform(
                        get("/api/users/{userId}/supports", userId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userSupportId")
                        .value(response.getUserSupportId().toString()));

        verify(userSupportService).getAll(userId);
    }

    @Test
    void update_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID userSupportId = UUID.randomUUID();
        UserSupportResponse response = new UserSupportResponse();
        response.setUserSupportId(userSupportId);
        response.setSelectedForAction(true);

        when(userSupportService.update(
                any(),
                any(),
                any()
        )).thenReturn(response);

        String request = """
                {
                  "selectedForAction": true,
                  "note": "Проверено пользователем"
                }
                """;

        mockMvc.perform(
                        patch(
                                "/api/users/{userId}/supports/{userSupportId}",
                                userId,
                                userSupportId
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.selectedForAction")
                        .value(true));

        verify(userSupportService).update(
                any(),
                any(),
                any()
        );
    }
}
