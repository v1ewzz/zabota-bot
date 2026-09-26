package ru.zabota.bot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.zabota.bot.dto.dictionary.DictionaryTypeResponse;
import ru.zabota.bot.dto.dictionary.DictionaryValueResponse;
import ru.zabota.bot.service.DictionaryService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Unit-тест DictionaryController.
 *
 * Проверяет HTTP-маршруты получения типов справочников,
 * значений конкретного справочника и отдельных значений.
 *
 * Spring-контекст не запускается.
 * DictionaryService заменён Mockito-моком.
 * MockMvc создаётся вручную через MockMvcBuilders.
 */
class DictionaryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DictionaryService dictionaryService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new DictionaryController(
                                dictionaryService
                        )
                )
                .build();
    }

    @Test
    void shouldGetDictionaryTypes() throws Exception {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();

        DictionaryTypeResponse first =
                createDictionaryTypeResponse(
                        firstId,
                        "MILITARY_STATUS",
                        "Военный статус"
                );

        DictionaryTypeResponse second =
                createDictionaryTypeResponse(
                        secondId,
                        "FAMILY_RELATION",
                        "Семейное положение"
                );

        when(dictionaryService.getDictionaryTypes())
                .thenReturn(
                        List.of(first, second)
                );

        mockMvc.perform(
                        get("/api/dictionaries")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].dictionaryTypeId")
                                .value(firstId.toString())
                )
                .andExpect(
                        jsonPath("$[0].code")
                                .value("MILITARY_STATUS")
                )
                .andExpect(
                        jsonPath("$[0].name")
                                .value("Военный статус")
                )
                .andExpect(
                        jsonPath("$[1].dictionaryTypeId")
                                .value(secondId.toString())
                );

        verify(dictionaryService)
                .getDictionaryTypes();
    }

    @Test
    void shouldGetDictionaryTypeById() throws Exception {
        UUID id = UUID.randomUUID();

        DictionaryTypeResponse response =
                createDictionaryTypeResponse(
                        id,
                        "MILITARY_STATUS",
                        "Военный статус"
                );

        when(dictionaryService.getDictionaryTypeById(id))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/dictionaries/{id}",
                                id
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.dictionaryTypeId")
                                .value(id.toString())
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("MILITARY_STATUS")
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Военный статус")
                );

        verify(dictionaryService)
                .getDictionaryTypeById(id);
    }

    @Test
    void shouldGetDictionaryValues() throws Exception {
        UUID dictionaryId =
                UUID.randomUUID();

        UUID firstValueId =
                UUID.randomUUID();

        UUID secondValueId =
                UUID.randomUUID();

        DictionaryValueResponse first =
                createDictionaryValueResponse(
                        firstValueId,
                        "MOBILIZED",
                        "Мобилизован"
                );

        DictionaryValueResponse second =
                createDictionaryValueResponse(
                        secondValueId,
                        "SVO",
                        "Участник СВО"
                );

        when(dictionaryService.getDictionaryValues(
                dictionaryId
        )).thenReturn(
                List.of(first, second)
        );

        mockMvc.perform(
                        get(
                                "/api/dictionaries/{id}/values",
                                dictionaryId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].dictionaryValueId")
                                .value(
                                        firstValueId.toString()
                                )
                )
                .andExpect(
                        jsonPath("$[0].code")
                                .value("MOBILIZED")
                )
                .andExpect(
                        jsonPath("$[0].label")
                                .value("Мобилизован")
                )
                .andExpect(
                        jsonPath("$[1].dictionaryValueId")
                                .value(
                                        secondValueId.toString()
                                )
                );

        verify(dictionaryService)
                .getDictionaryValues(dictionaryId);
    }

    @Test
    void shouldGetDictionaryValueById() throws Exception {
        UUID id = UUID.randomUUID();

        DictionaryValueResponse response =
                createDictionaryValueResponse(
                        id,
                        "MOBILIZED",
                        "Мобилизован"
                );

        when(dictionaryService.getDictionaryValueById(id))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/dictionaries/values/{id}",
                                id
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.dictionaryValueId")
                                .value(id.toString())
                )
                .andExpect(
                        jsonPath("$.code")
                                .value("MOBILIZED")
                )
                .andExpect(
                        jsonPath("$.label")
                                .value("Мобилизован")
                );

        verify(dictionaryService)
                .getDictionaryValueById(id);
    }

    private DictionaryTypeResponse createDictionaryTypeResponse(
            UUID id,
            String code,
            String name
    ) {
        DictionaryTypeResponse response =
                new DictionaryTypeResponse();

        response.setDictionaryTypeId(id);
        response.setCode(code);
        response.setName(name);

        return response;
    }

    private DictionaryValueResponse createDictionaryValueResponse(
            UUID id,
            String code,
            String label
    ) {
        DictionaryValueResponse response =
                new DictionaryValueResponse();

        response.setDictionaryValueId(id);
        response.setCode(code);
        response.setLabel(label);

        return response;
    }

    @Test
    void getDictionaryTypeByCode_shouldReturn200() throws Exception {
        UUID typeId = UUID.randomUUID();
        DictionaryTypeResponse response = new DictionaryTypeResponse();
        response.setDictionaryTypeId(typeId);
        response.setCode("MILITARY_STATUS");
        response.setName("Статус военнослужащего");

        when(dictionaryService.getDictionaryTypeByCode("MILITARY_STATUS"))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/dictionaries/by-code/MILITARY_STATUS")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dictionaryTypeId")
                        .value(typeId.toString()))
                .andExpect(jsonPath("$.code")
                        .value("MILITARY_STATUS"));
    }

    @Test
    void getDictionaryValuesByCode_shouldReturn200() throws Exception {
        DictionaryValueResponse value = new DictionaryValueResponse();
        value.setDictionaryValueId(UUID.randomUUID());
        value.setCode("MOBILIZED");
        value.setLabel("Мобилизованный");

        when(dictionaryService.getDictionaryValuesByCode("MILITARY_STATUS"))
                .thenReturn(List.of(value));

        mockMvc.perform(
                        get("/api/dictionaries/by-code/MILITARY_STATUS/values")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code")
                        .value("MOBILIZED"));
    }

}