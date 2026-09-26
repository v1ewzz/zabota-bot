package ru.zabota.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zabota.bot.dto.region.RegionRequest;
import ru.zabota.bot.dto.region.RegionResponse;
import ru.zabota.bot.service.RegionService;

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
 * Unit-тест RegionController.
 *
 * Проверяет HTTP-маршруты контроллера:
 *
 * - POST /api/regions;
 * - GET /api/regions;
 * - GET /api/regions/{id};
 * - PUT /api/regions/{id};
 * - DELETE /api/regions/{id}.
 *
 * Также проверяет валидацию RegionRequest.
 *
 * Spring-контекст не запускается.
 * RegionService заменён Mockito-моком.
 * MockMvc создаётся вручную через MockMvcBuilders.
 * ObjectMapper создаётся вручную.
 */
class RegionControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private RegionService regionService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new RegionController(regionService)
                )
                .build();
    }

    @Test
    void shouldCreateRegion() throws Exception {
        UUID regionId = UUID.randomUUID();

        RegionRequest request = createRequest();

        RegionResponse response =
                createResponse(regionId);

        when(regionService.create(any(RegionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/regions")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.regionId")
                                .value(regionId.toString())
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Республика Татарстан"
                                )
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("16")
                );

        verify(regionService)
                .create(any(RegionRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {

        RegionRequest request =
                new RegionRequest();

        request.setName("");
        request.setCode("16");

        mockMvc.perform(
                        post("/api/regions")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest());

        verify(
                regionService,
                never()
        ).create(any(RegionRequest.class));
    }

    @Test
    void shouldGetAllRegions() throws Exception {
        RegionResponse first =
                createResponse(
                        UUID.randomUUID()
                );

        RegionResponse second =
                createResponse(
                        UUID.randomUUID()
                );

        when(regionService.getAll())
                .thenReturn(
                        List.of(first, second)
                );

        mockMvc.perform(
                        get("/api/regions")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                );

        verify(regionService)
                .getAll();
    }

    @Test
    void shouldGetRegionById() throws Exception {
        UUID regionId =
                UUID.randomUUID();

        RegionResponse response =
                createResponse(regionId);

        when(regionService.getById(regionId))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/regions/{id}",
                                regionId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.regionId")
                                .value(
                                        regionId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Республика Татарстан"
                                )
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("16")
                );

        verify(regionService)
                .getById(regionId);
    }

    @Test
    void shouldUpdateRegion() throws Exception {
        UUID regionId =
                UUID.randomUUID();

        RegionRequest request =
                createRequest();

        RegionResponse response =
                createResponse(regionId);

        when(
                regionService.update(
                        eq(regionId),
                        any(RegionRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        put(
                                "/api/regions/{id}",
                                regionId
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
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.regionId")
                                .value(
                                        regionId.toString()
                                )
                );

        verify(regionService)
                .update(
                        eq(regionId),
                        any(RegionRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        UUID regionId =
                UUID.randomUUID();

        RegionRequest request =
                new RegionRequest();

        request.setName("");
        request.setCode("16");

        mockMvc.perform(
                        put(
                                "/api/regions/{id}",
                                regionId
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
                .andExpect(status().isBadRequest());

        verify(
                regionService,
                never()
        ).update(
                eq(regionId),
                any(RegionRequest.class)
        );
    }

    @Test
    void shouldDeleteRegion() throws Exception {
        UUID regionId =
                UUID.randomUUID();

        mockMvc.perform(
                        delete(
                                "/api/regions/{id}",
                                regionId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(regionService)
                .delete(regionId);
    }

    private RegionRequest createRequest() {
        RegionRequest request =
                new RegionRequest();

        request.setName(
                "Республика Татарстан"
        );

        request.setCode("16");

        return request;
    }

    private RegionResponse createResponse(
            UUID id
    ) {
        RegionResponse response =
                new RegionResponse();

        response.setRegionId(id);
        response.setName(
                "Республика Татарстан"
        );
        response.setCode("16");

        return response;
    }
}