package ru.zabota.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zabota.bot.dto.municipality.MunicipalityRequest;
import ru.zabota.bot.dto.municipality.MunicipalityResponse;
import ru.zabota.bot.service.MunicipalityService;

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
 * Unit-тест MunicipalityController.
 *
 * Проверяет HTTP-маршруты, JSON-запросы и ответы,
 * HTTP-статусы, валидацию MunicipalityRequest
 * и взаимодействие контроллера с MunicipalityService.
 *
 * Spring-контекст не запускается.
 * MockMvc создаётся через MockMvcBuilders.
 * MunicipalityService заменён Mockito-моком.
 */
class MunicipalityControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private MunicipalityService municipalityService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new MunicipalityController(
                                municipalityService
                        )
                )
                .build();
    }

    @Test
    void shouldCreateMunicipality() throws Exception {
        UUID regionId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        UUID municipalityId = UUID.randomUUID();

        MunicipalityRequest request =
                createRequest(
                        regionId,
                        typeId
                );

        MunicipalityResponse response =
                createResponse(
                        municipalityId,
                        regionId,
                        typeId
                );

        when(
                municipalityService.create(
                        any(MunicipalityRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        post("/api/municipalities")
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
                        jsonPath("$.municipalityId")
                                .value(
                                        municipalityId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Казань")
                )
                .andExpect(
                        jsonPath("$.district")
                                .value("город Казань")
                )
                .andExpect(
                        jsonPath("$.regionId")
                                .value(
                                        regionId.toString()
                                )
                );

        verify(municipalityService)
                .create(
                        any(MunicipalityRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {

        MunicipalityRequest request =
                new MunicipalityRequest();

        request.setName("");
        request.setRegionId(
                UUID.randomUUID()
        );
        request.setTypeId(
                UUID.randomUUID()
        );

        mockMvc.perform(
                        post("/api/municipalities")
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
                municipalityService,
                never()
        ).create(
                any(MunicipalityRequest.class)
        );
    }

    @Test
    void shouldGetAllMunicipalities()
            throws Exception {

        MunicipalityResponse first =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        MunicipalityResponse second =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        when(municipalityService.getAll())
                .thenReturn(
                        List.of(first, second)
                );

        mockMvc.perform(
                        get("/api/municipalities")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                );

        verify(municipalityService)
                .getAll();
    }

    @Test
    void shouldGetMunicipalityById()
            throws Exception {

        UUID municipalityId =
                UUID.randomUUID();

        UUID regionId =
                UUID.randomUUID();

        UUID typeId =
                UUID.randomUUID();

        MunicipalityResponse response =
                createResponse(
                        municipalityId,
                        regionId,
                        typeId
                );

        when(
                municipalityService.getById(
                        municipalityId
                )
        ).thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/municipalities/{id}",
                                municipalityId
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.municipalityId")
                                .value(
                                        municipalityId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Казань")
                )
                .andExpect(
                        jsonPath("$.regionId")
                                .value(
                                        regionId.toString()
                                )
                );

        verify(municipalityService)
                .getById(municipalityId);
    }

    @Test
    void shouldGetMunicipalitiesByRegion()
            throws Exception {

        UUID regionId =
                UUID.randomUUID();

        MunicipalityResponse first =
                createResponse(
                        UUID.randomUUID(),
                        regionId,
                        UUID.randomUUID()
                );

        MunicipalityResponse second =
                createResponse(
                        UUID.randomUUID(),
                        regionId,
                        UUID.randomUUID()
                );

        when(
                municipalityService.getByRegion(
                        regionId
                )
        ).thenReturn(
                List.of(first, second)
        );

        mockMvc.perform(
                        get(
                                "/api/municipalities/by-region/{regionId}",
                                regionId
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].regionId")
                                .value(
                                        regionId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$[1].regionId")
                                .value(
                                        regionId.toString()
                                )
                );

        verify(municipalityService)
                .getByRegion(regionId);
    }

    @Test
    void shouldUpdateMunicipality()
            throws Exception {

        UUID municipalityId =
                UUID.randomUUID();

        UUID regionId =
                UUID.randomUUID();

        UUID typeId =
                UUID.randomUUID();

        MunicipalityRequest request =
                createRequest(
                        regionId,
                        typeId
                );

        MunicipalityResponse response =
                createResponse(
                        municipalityId,
                        regionId,
                        typeId
                );

        when(
                municipalityService.update(
                        eq(municipalityId),
                        any(MunicipalityRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        put(
                                "/api/municipalities/{id}",
                                municipalityId
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
                        jsonPath("$.municipalityId")
                                .value(
                                        municipalityId.toString()
                                )
                );

        verify(municipalityService)
                .update(
                        eq(municipalityId),
                        any(MunicipalityRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        UUID municipalityId =
                UUID.randomUUID();

        MunicipalityRequest request =
                new MunicipalityRequest();

        request.setName("");
        request.setRegionId(
                UUID.randomUUID()
        );
        request.setTypeId(
                UUID.randomUUID()
        );

        mockMvc.perform(
                        put(
                                "/api/municipalities/{id}",
                                municipalityId
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
                municipalityService,
                never()
        ).update(
                eq(municipalityId),
                any(MunicipalityRequest.class)
        );
    }

    @Test
    void shouldDeleteMunicipality()
            throws Exception {

        UUID municipalityId =
                UUID.randomUUID();

        mockMvc.perform(
                        delete(
                                "/api/municipalities/{id}",
                                municipalityId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(municipalityService)
                .delete(municipalityId);
    }

    private MunicipalityRequest createRequest(
            UUID regionId,
            UUID typeId
    ) {
        MunicipalityRequest request =
                new MunicipalityRequest();

        request.setRegionId(regionId);
        request.setName("Казань");
        request.setDistrict("город Казань");
        request.setTypeId(typeId);

        return request;
    }

    private MunicipalityResponse createResponse(
            UUID municipalityId,
            UUID regionId,
            UUID typeId
    ) {
        MunicipalityResponse response =
                new MunicipalityResponse();

        response.setMunicipalityId(
                municipalityId
        );

        response.setRegionId(regionId);
        response.setRegionName(
                "Республика Татарстан"
        );

        response.setName("Казань");
        response.setDistrict("город Казань");

        response.setTypeId(typeId);
        response.setTypeCode("CITY");
        response.setTypeName("Город");

        return response;
    }
}