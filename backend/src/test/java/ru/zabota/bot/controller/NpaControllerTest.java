package ru.zabota.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zabota.bot.dto.npa.NpaRequest;
import ru.zabota.bot.dto.npa.NpaResponse;
import ru.zabota.bot.service.NpaService;

import java.time.LocalDate;
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
 * Unit-тест NpaController.
 *
 * Проверяет:
 *
 * - создание НПА;
 * - получение всех НПА;
 * - получение НПА по id;
 * - обновление НПА;
 * - удаление НПА;
 * - валидацию NpaRequest.
 *
 * Spring-контекст не запускается.
 * NpaService заменён Mockito-моком.
 * MockMvc создаётся вручную через MockMvcBuilders.
 */
class NpaControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private NpaService npaService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new NpaController(npaService)
                )
                .build();
    }

    @Test
    void shouldCreateNpa() throws Exception {
        UUID npaId = UUID.randomUUID();
        UUID npaTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        NpaRequest request =
                createRequest(
                        npaTypeId,
                        levelId,
                        statusId
                );

        NpaResponse response =
                createResponse(
                        npaId,
                        npaTypeId,
                        levelId,
                        statusId
                );

        when(
                npaService.create(
                        any(NpaRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        post("/api/npas")
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
                        jsonPath("$.npaId")
                                .value(npaId.toString())
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Закон Республики Татарстан"
                                )
                )
                .andExpect(
                        jsonPath("$.number")
                                .value("100-ЗРТ")
                )
                .andExpect(
                        jsonPath("$.officialUrl")
                                .value(
                                        "https://pravo.tatarstan.ru/"
                                )
                );

        verify(npaService)
                .create(
                        any(NpaRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {

        NpaRequest request =
                new NpaRequest();

        request.setName("");
        request.setNumber("");
        request.setOfficialUrl("");

        mockMvc.perform(
                        post("/api/npas")
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
                npaService,
                never()
        ).create(
                any(NpaRequest.class)
        );
    }

    @Test
    void shouldGetAllNpa() throws Exception {
        NpaResponse first =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        NpaResponse second =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        when(npaService.getAll())
                .thenReturn(
                        List.of(first, second)
                );

        mockMvc.perform(
                        get("/api/npas")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].name")
                                .value(
                                        "Закон Республики Татарстан"
                                )
                )
                .andExpect(
                        jsonPath("$[1].number")
                                .value("100-ЗРТ")
                );

        verify(npaService)
                .getAll();
    }

    @Test
    void shouldGetNpaById() throws Exception {
        UUID npaId = UUID.randomUUID();
        UUID npaTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        NpaResponse response =
                createResponse(
                        npaId,
                        npaTypeId,
                        levelId,
                        statusId
                );

        when(npaService.getById(npaId))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/npas/{id}",
                                npaId
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.npaId")
                                .value(
                                        npaId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Закон Республики Татарстан"
                                )
                )
                .andExpect(
                        jsonPath("$.number")
                                .value("100-ЗРТ")
                )
                .andExpect(
                        jsonPath("$.npaTypeId")
                                .value(
                                        npaTypeId.toString()
                                )
                );

        verify(npaService)
                .getById(npaId);
    }

    @Test
    void shouldUpdateNpa() throws Exception {
        UUID npaId = UUID.randomUUID();
        UUID npaTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        NpaRequest request =
                createRequest(
                        npaTypeId,
                        levelId,
                        statusId
                );

        NpaResponse response =
                createResponse(
                        npaId,
                        npaTypeId,
                        levelId,
                        statusId
                );

        when(
                npaService.update(
                        eq(npaId),
                        any(NpaRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        put(
                                "/api/npas/{id}",
                                npaId
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
                        jsonPath("$.npaId")
                                .value(
                                        npaId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Закон Республики Татарстан"
                                )
                );

        verify(npaService)
                .update(
                        eq(npaId),
                        any(NpaRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        UUID npaId = UUID.randomUUID();

        NpaRequest request =
                new NpaRequest();

        request.setName("");
        request.setNumber("");
        request.setOfficialUrl("");

        mockMvc.perform(
                        put(
                                "/api/npas/{id}",
                                npaId
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
                npaService,
                never()
        ).update(
                eq(npaId),
                any(NpaRequest.class)
        );
    }

    @Test
    void shouldDeleteNpa() throws Exception {
        UUID npaId = UUID.randomUUID();

        mockMvc.perform(
                        delete(
                                "/api/npas/{id}",
                                npaId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(npaService)
                .delete(npaId);
    }

    private NpaRequest createRequest(
            UUID npaTypeId,
            UUID levelId,
            UUID statusId
    ) {
        NpaRequest request =
                new NpaRequest();

        request.setName(
                "Закон Республики Татарстан"
        );

        request.setNpaTypeId(
                npaTypeId
        );

        request.setNumber(
                "100-ЗРТ"
        );

        request.setAdoptionDate(
                LocalDate.of(
                        2024,
                        12,
                        25
                )
        );

        request.setValidFrom(
                LocalDate.of(
                        2025,
                        1,
                        1
                )
        );

        request.setValidTo(null);

        request.setLevelId(
                levelId
        );

        request.setStatusId(
                statusId
        );

        request.setOfficialUrl(
                "https://pravo.tatarstan.ru/"
        );

        return request;
    }

    private NpaResponse createResponse(
            UUID npaId,
            UUID npaTypeId,
            UUID levelId,
            UUID statusId
    ) {
        NpaResponse response =
                new NpaResponse();

        response.setNpaId(npaId);

        response.setName(
                "Закон Республики Татарстан"
        );

        response.setNumber(
                "100-ЗРТ"
        );

        response.setAdoptionDate(
                LocalDate.of(
                        2024,
                        12,
                        25
                )
        );

        response.setValidFrom(
                LocalDate.of(
                        2025,
                        1,
                        1
                )
        );

        response.setValidTo(null);

        response.setNpaTypeId(
                npaTypeId
        );

        response.setNpaTypeCode(
                "LAW"
        );

        response.setNpaTypeName(
                "Закон"
        );

        response.setLevelId(
                levelId
        );

        response.setLevelCode(
                "REGIONAL"
        );

        response.setLevelName(
                "Региональный"
        );

        response.setStatusId(
                statusId
        );

        response.setStatusCode(
                "ACTIVE"
        );

        response.setStatusName(
                "Действующий"
        );

        response.setOfficialUrl(
                "https://pravo.tatarstan.ru/"
        );

        return response;
    }
}