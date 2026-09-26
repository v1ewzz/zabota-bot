package ru.zabota.bot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.zabota.bot.dto.support.SupportMeasureShortResponse;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.service.SupportMatchingService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupportMatchingController.class)
class SupportMatchingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SupportMatchingService supportMatchingService;

    @Test
    void search_shouldReturn200AndMeasures() throws Exception {
        UUID supportId = UUID.randomUUID();

        SupportMeasureShortResponse measure =
                new SupportMeasureShortResponse();

        measure.setSupportId(supportId);
        measure.setName("Мера по возрасту");
        measure.setDescription("Описание меры");
        measure.setAmount(new BigDecimal("3000.00"));
        measure.setFrequency("MONTHLY");
        measure.setApplicationRequired(true);
        measure.setApplicationChannel("MFC");
        measure.setActionUrl("https://example.com");

        SupportSearchResponse response =
                new SupportSearchResponse();

        response.setUserId(UUID.randomUUID());
        response.setMeasures(List.of(measure));

        when(supportMatchingService.search(any()))
                .thenReturn(response);

        String requestBody = """
                {
                  "regionId": "11111111-1111-1111-1111-111111111111",
                  "municipalityId": "22222222-2222-2222-2222-222222222222",
                  "familyRelationId": null,
                  "militaryStatusId": null,
                  "pregnancy": false,
                  "injury": false,
                  "disability": false,
                  "disabilityGroupId": null,
                  "housingProblem": false,
                  "gasificationNeeded": false,
                  "employmentStatusId": null,
                  "incomeCategoryId": null,
                  "children": []
                }
                """;

        mockMvc.perform(
                        post("/api/supports/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.measures").isArray())
                .andExpect(jsonPath("$.measures.length()").value(1))
                .andExpect(jsonPath("$.measures[0].supportId")
                        .value(supportId.toString()))
                .andExpect(jsonPath("$.measures[0].name")
                        .value("Мера по возрасту"))
                .andExpect(jsonPath("$.measures[0].amount")
                        .value(3000.00));

        verify(supportMatchingService)
                .search(any());
    }

    @Test
    void search_shouldReturn400_whenServiceThrowsBadRequestException()
            throws Exception {

        when(supportMatchingService.search(any()))
                .thenThrow(
                        new ru.zabota.bot.exception.BadRequestException(
                                "Регион не указан"
                        )
                );

        String requestBody = """
                {
                  "regionId": null,
                  "municipalityId": "22222222-2222-2222-2222-222222222222",
                  "familyRelationId": null,
                  "militaryStatusId": null,
                  "pregnancy": false,
                  "injury": false,
                  "disability": false,
                  "disabilityGroupId": null,
                  "housingProblem": false,
                  "gasificationNeeded": false,
                  "employmentStatusId": null,
                  "incomeCategoryId": null,
                  "children": []
                }
                """;

        mockMvc.perform(
                        post("/api/supports/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }
}