package ru.zabota.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zabota.bot.dto.support.SupportRuleRequest;
import ru.zabota.bot.dto.support.SupportRuleResponse;
import ru.zabota.bot.service.SupportRuleService;

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
 * Unit-тест SupportRuleController.
 *
 * Проверяет HTTP-маршруты, JSON-запросы и ответы,
 * HTTP-статусы, валидацию SupportRuleRequest
 * и взаимодействие контроллера с SupportRuleService.
 *
 * Spring-контекст не запускается.
 * MockMvc создаётся вручную через MockMvcBuilders.
 * SupportRuleService заменён Mockito-моком.
 */
class SupportRuleControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private SupportRuleService supportRuleService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new SupportRuleController(
                                supportRuleService
                        )
                )
                .build();
    }

    @Test
    void shouldCreateSupportRule() throws Exception {
        UUID ruleId = UUID.randomUUID();
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleRequest request =
                createRequest(
                        supportId,
                        operatorId
                );

        SupportRuleResponse response =
                createResponse(
                        ruleId,
                        supportId,
                        operatorId
                );

        when(
                supportRuleService.create(
                        any(SupportRuleRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        post("/api/support-rules")
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
                        jsonPath("$.ruleId")
                                .value(
                                        ruleId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.supportId")
                                .value(
                                        supportId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.parameter")
                                .value(
                                        "military_status"
                                )
                )
                .andExpect(
                        jsonPath("$.valueType")
                                .value(
                                        "REFERENCE"
                                )
                )
                .andExpect(
                        jsonPath("$.value")
                                .value(
                                        "MOBILIZED"
                                )
                );

        verify(supportRuleService)
                .create(
                        any(SupportRuleRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {

        SupportRuleRequest request =
                new SupportRuleRequest();

        request.setSupportId(null);
        request.setParameter("");
        request.setOperatorId(null);
        request.setValueType("");

        mockMvc.perform(
                        post("/api/support-rules")
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
                supportRuleService,
                never()
        ).create(
                any(SupportRuleRequest.class)
        );
    }

    @Test
    void shouldGetAllSupportRules()
            throws Exception {

        SupportRuleResponse first =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        SupportRuleResponse second =
                createResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        when(
                supportRuleService.getAll()
        ).thenReturn(
                List.of(first, second)
        );

        mockMvc.perform(
                        get("/api/support-rules")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].parameter")
                                .value(
                                        "military_status"
                                )
                )
                .andExpect(
                        jsonPath("$[1].value")
                                .value(
                                        "MOBILIZED"
                                )
                );

        verify(supportRuleService)
                .getAll();
    }

    @Test
    void shouldGetSupportRuleById()
            throws Exception {

        UUID ruleId = UUID.randomUUID();
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleResponse response =
                createResponse(
                        ruleId,
                        supportId,
                        operatorId
                );

        when(
                supportRuleService.getById(ruleId)
        ).thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/support-rules/{id}",
                                ruleId
                        )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.ruleId")
                                .value(
                                        ruleId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.supportId")
                                .value(
                                        supportId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.parameter")
                                .value(
                                        "military_status"
                                )
                );

        verify(supportRuleService)
                .getById(ruleId);
    }

    @Test
    void shouldGetSupportRulesBySupportId()
            throws Exception {

        UUID supportId =
                UUID.randomUUID();

        SupportRuleResponse first =
                createResponse(
                        UUID.randomUUID(),
                        supportId,
                        UUID.randomUUID()
                );

        SupportRuleResponse second =
                createResponse(
                        UUID.randomUUID(),
                        supportId,
                        UUID.randomUUID()
                );

        when(
                supportRuleService.getBySupportId(
                        supportId
                )
        ).thenReturn(
                List.of(first, second)
        );

        mockMvc.perform(
                        get(
                                "/api/support-rules/support/{supportId}",
                                supportId
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
                        jsonPath("$[0].supportId")
                                .value(
                                        supportId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$[1].supportId")
                                .value(
                                        supportId.toString()
                                )
                );

        verify(supportRuleService)
                .getBySupportId(supportId);
    }

    @Test
    void shouldUpdateSupportRule()
            throws Exception {

        UUID ruleId = UUID.randomUUID();
        UUID supportId = UUID.randomUUID();
        UUID operatorId = UUID.randomUUID();

        SupportRuleRequest request =
                createRequest(
                        supportId,
                        operatorId
                );

        SupportRuleResponse response =
                createResponse(
                        ruleId,
                        supportId,
                        operatorId
                );

        when(
                supportRuleService.update(
                        eq(ruleId),
                        any(SupportRuleRequest.class)
                )
        ).thenReturn(response);

        mockMvc.perform(
                        put(
                                "/api/support-rules/{id}",
                                ruleId
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
                        jsonPath("$.ruleId")
                                .value(
                                        ruleId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$.supportId")
                                .value(
                                        supportId.toString()
                                )
                );

        verify(supportRuleService)
                .update(
                        eq(ruleId),
                        any(SupportRuleRequest.class)
                );
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        UUID ruleId =
                UUID.randomUUID();

        SupportRuleRequest request =
                new SupportRuleRequest();

        request.setSupportId(null);
        request.setParameter("");
        request.setOperatorId(null);
        request.setValueType("");

        mockMvc.perform(
                        put(
                                "/api/support-rules/{id}",
                                ruleId
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
                supportRuleService,
                never()
        ).update(
                eq(ruleId),
                any(SupportRuleRequest.class)
        );
    }

    @Test
    void shouldDeleteSupportRule()
            throws Exception {

        UUID ruleId =
                UUID.randomUUID();

        mockMvc.perform(
                        delete(
                                "/api/support-rules/{id}",
                                ruleId
                        )
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(supportRuleService)
                .delete(ruleId);
    }

    private SupportRuleRequest createRequest(
            UUID supportId,
            UUID operatorId
    ) {
        SupportRuleRequest request =
                new SupportRuleRequest();

        request.setSupportId(supportId);
        request.setParameter(
                "military_status"
        );
        request.setOperatorId(operatorId);
        request.setValueType(
                "REFERENCE"
        );
        request.setValueFrom(null);
        request.setValueTo(null);
        request.setValue(
                "MOBILIZED"
        );
        request.setConditionGroup(1);
        request.setRequired(true);
        request.setAmountOverride(null);

        return request;
    }

    private SupportRuleResponse createResponse(
            UUID ruleId,
            UUID supportId,
            UUID operatorId
    ) {
        SupportRuleResponse response =
                new SupportRuleResponse();

        response.setRuleId(ruleId);

        response.setSupportId(supportId);

        response.setParameter(
                "military_status"
        );

        response.setOperatorId(operatorId);

        response.setValueType(
                "REFERENCE"
        );

        response.setValueFrom(null);
        response.setValueTo(null);

        response.setValue(
                "MOBILIZED"
        );

        response.setConditionGroup(1);
        response.setRequired(true);
        response.setAmountOverride(null);

        return response;
    }
}