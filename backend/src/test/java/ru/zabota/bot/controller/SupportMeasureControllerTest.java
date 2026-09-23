package ru.zabota.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zabota.bot.dto.support.SupportMeasureRequest;
import ru.zabota.bot.dto.support.SupportMeasureResponse;
import ru.zabota.bot.service.SupportMeasureService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Unit-тест SupportMeasureController.
 *
 * Проверяет:
 *
 * - создание меры поддержки;
 * - получение всех мер;
 * - получение меры по id;
 * - обновление меры;
 * - удаление меры;
 * - валидацию SupportMeasureRequest.
 *
 * Spring-контекст не запускается.
 * SupportMeasureService заменён Mockito-моком.
 * MockMvc и ObjectMapper создаются вручную.
 */
class SupportMeasureControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private SupportMeasureService supportMeasureService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new SupportMeasureController(
                                supportMeasureService
                        )
                )
                .build();
    }
    @Test
    void shouldCreateSupportMeasure() throws Exception {
        UUID supportId = UUID.randomUUID();

        UUID supportTypeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID recipientTypeId = UUID.randomUUID();
        UUID verificationStatusId = UUID.randomUUID();

        SupportMeasureRequest request =
                createRequest(
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        verificationStatusId
                );

        SupportMeasureResponse response =
                createResponse(
                        supportId,
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        verificationStatusId
                );

        when(
                supportMeasureService.create(
                        any(SupportMeasureRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        post("/api/support-measures")
                                .contentType(APPLICATION_JSON)
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
                        jsonPath("$.supportId")
                                .value(
                                        supportId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Ежемесячная выплата"
                                )
                )
                .andExpect(
                        jsonPath("$.description")
                                .value(
                                        "Мера социальной поддержки семьи"
                                )
                )
                .andExpect(
                        jsonPath("$.amount")
                                .value(20000.00)
                );

        verify(supportMeasureService)
                .create(
                        any(SupportMeasureRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {

        SupportMeasureRequest request =
                new SupportMeasureRequest();

        request.setName("");
        request.setDescription("");
        request.setApplicationRequired(true);

        mockMvc.perform(
                        post("/api/support-measures")
                                .contentType(APPLICATION_JSON)
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
                supportMeasureService,
                never()
        ).create(
                any(SupportMeasureRequest.class)
        );
    }

    @Test
    void shouldGetAllSupportMeasures()
            throws Exception {

        SupportMeasureResponse first =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        SupportMeasureResponse second =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        when(supportMeasureService.getAll())
                .thenReturn(
                        List.of(first, second)
                );

        mockMvc.perform(
                        get("/api/support-measures")
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
                                        "Ежемесячная выплата"
                                )
                );

        verify(supportMeasureService)
                .getAll();
    }

    @Test
    void shouldGetSupportMeasureById()
            throws Exception {

        UUID supportId =
                UUID.randomUUID();

        UUID supportTypeId =
                UUID.randomUUID();

        UUID levelId =
                UUID.randomUUID();

        UUID recipientTypeId =
                UUID.randomUUID();

        UUID verificationStatusId =
                UUID.randomUUID();

        SupportMeasureResponse response =
                createResponse(
                        supportId,
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        verificationStatusId
                );

        when(
                supportMeasureService.getById(
                        supportId
                )
        ).thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/support-measures/{id}",
                                supportId
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.supportId")
                                .value(
                                        supportId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Ежемесячная выплата"
                                )
                )
                .andExpect(
                        jsonPath("$.description")
                                .value(
                                        "Мера социальной поддержки семьи"
                                )
                )
                .andExpect(
                        jsonPath("$.supportTypeId")
                                .value(
                                        supportTypeId.toString()
                                )
                );

        verify(supportMeasureService)
                .getById(supportId);
    }

    @Test
    void shouldUpdateSupportMeasure()
            throws Exception {

        UUID supportId =
                UUID.randomUUID();

        UUID supportTypeId =
                UUID.randomUUID();

        UUID levelId =
                UUID.randomUUID();

        UUID recipientTypeId =
                UUID.randomUUID();

        UUID verificationStatusId =
                UUID.randomUUID();

        SupportMeasureRequest request =
                createRequest(
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        verificationStatusId
                );

        SupportMeasureResponse response =
                createResponse(
                        supportId,
                        supportTypeId,
                        levelId,
                        recipientTypeId,
                        verificationStatusId
                );

        when(
                supportMeasureService.update(
                        eq(supportId),
                        any(SupportMeasureRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        put(
                                "/api/support-measures/{id}",
                                supportId
                        )
                                .contentType(APPLICATION_JSON)
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
                        jsonPath("$.supportId")
                                .value(
                                        supportId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.name")
                                .value(
                                        "Ежемесячная выплата"
                                )
                );

        verify(supportMeasureService)
                .update(
                        eq(supportId),
                        any(SupportMeasureRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        UUID supportId =
                UUID.randomUUID();

        SupportMeasureRequest request =
                new SupportMeasureRequest();

        request.setName("");
        request.setDescription("");
        request.setApplicationRequired(true);

        mockMvc.perform(
                        put(
                                "/api/support-measures/{id}",
                                supportId
                        )
                                .contentType(APPLICATION_JSON)
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
                supportMeasureService,
                never()
        ).update(
                eq(supportId),
                any(SupportMeasureRequest.class)
        );
    }

    @Test
    void shouldDeleteSupportMeasure()
            throws Exception {

        UUID supportId =
                UUID.randomUUID();

        mockMvc.perform(
                        delete(
                                "/api/support-measures/{id}",
                                supportId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(supportMeasureService)
                .delete(supportId);
    }

    private SupportMeasureRequest createRequest(
            UUID supportTypeId,
            UUID levelId,
            UUID recipientTypeId,
            UUID verificationStatusId
    ) {
        SupportMeasureRequest request =
                new SupportMeasureRequest();

        request.setName(
                "Ежемесячная выплата"
        );

        request.setDescription(
                "Мера социальной поддержки семьи"
        );

        request.setSupportTypeId(
                supportTypeId
        );

        request.setLevelId(
                levelId
        );

        request.setRecipientTypeId(
                recipientTypeId
        );

        request.setApplicationRequired(
                true
        );

        request.setApplicationChannelId(
                null
        );

        request.setAmount(
                new BigDecimal("20000.00")
        );

        request.setFrequencyId(
                null
        );

        request.setDocuments(
                "Паспорт, документы о составе семьи"
        );

        request.setValidFrom(
                LocalDate.of(
                        2025,
                        1,
                        1
                )
        );

        request.setValidTo(null);

        request.setVerificationStatusId(
                verificationStatusId
        );

        request.setActionUrl(
                "https://example.com/apply"
        );

        return request;
    }

    private SupportMeasureResponse createResponse(
            UUID supportId,
            UUID supportTypeId,
            UUID levelId,
            UUID recipientTypeId,
            UUID verificationStatusId
    ) {
        SupportMeasureResponse response =
                new SupportMeasureResponse();

        response.setSupportId(
                supportId
        );

        response.setName(
                "Ежемесячная выплата"
        );

        response.setDescription(
                "Мера социальной поддержки семьи"
        );

        response.setSupportTypeId(
                supportTypeId
        );

        response.setSupportTypeCode(
                "PAYMENT"
        );

        response.setSupportTypeName(
                "Выплата"
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

        response.setRecipientTypeId(
                recipientTypeId
        );

        response.setRecipientTypeCode(
                "FAMILY"
        );

        response.setRecipientTypeName(
                "Семья"
        );

        response.setApplicationRequired(
                true
        );

        response.setAmount(
                new BigDecimal("20000.00")
        );

        response.setVerificationStatusId(
                verificationStatusId
        );

        response.setVerificationStatusCode(
                "VERIFIED"
        );

        response.setVerificationStatusName(
                "Проверено"
        );

        response.setDocuments(
                "Паспорт, документы о составе семьи"
        );

        response.setActionUrl(
                "https://example.com/apply"
        );

        return response;
    }
}