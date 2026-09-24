package ru.zabota.bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.support.SupportSearchRequest;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.entity.SupportMunicipality;
import ru.zabota.bot.entity.SupportRule;
import ru.zabota.bot.exception.BadRequestException;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.RegionRepository;
import ru.zabota.bot.repository.SupportMeasureRepository;
import ru.zabota.bot.repository.SupportMunicipalityRepository;
import ru.zabota.bot.repository.SupportRuleRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/*
 * Unit-тесты SupportMatchingService.
 *
 * Проверяют основные сценарии алгоритма подбора:
 *
 * - EQUALS;
 * - IN;
 * - BETWEEN;
 * - GTE;
 * - AND внутри condition_group;
 * - OR между condition_group;
 * - правила для ребёнка;
 * - amount_override;
 * - необязательные правила;
 * - отсутствие правил;
 * - срок действия меры;
 * - обязательность входных параметров.
 *
 * Mockito работает в strict-режиме.
 * Поэтому каждый stub создаётся только внутри того теста,
 * который действительно вызывает соответствующий mock.
 */
@ExtendWith(MockitoExtension.class)
class SupportMatchingServiceTest {

    @Mock
    private SupportMeasureRepository supportMeasureRepository;

    @Mock
    private SupportMunicipalityRepository supportMunicipalityRepository;

    @Mock
    private SupportRuleRepository supportRuleRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private RegionRepository regionRepository;

    private SupportMatchingService service;

    private final UUID regionId = UUID.randomUUID();
    private final UUID municipalityId = UUID.randomUUID();

    private final UUID militaryStatusId = UUID.randomUUID();
    private final UUID educationLevelId = UUID.randomUUID();

    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2026-09-23T00:00:00Z"),
            ZoneOffset.UTC
    );

    @BeforeEach
    void setUp() {
        service = new SupportMatchingService(
                supportMeasureRepository,
                supportMunicipalityRepository,
                supportRuleRepository,
                dictionaryValueRepository,
                regionRepository,
                fixedClock
        );
    }

    @Test
    void search_shouldReturnMeasure_whenEqualsRuleMatches() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера для мобилизованных",
                new BigDecimal("10000.00")
        );

        DictionaryValue militaryStatus = dictionary(
                militaryStatusId,
                "MOBILIZED"
        );

        SupportRule rule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "military_status_id",
                "REFERENCE",
                null,
                null,
                "MOBILIZED",
                1,
                true,
                null
        );

        stubBaseDependencies(region, measure);

        when(dictionaryValueRepository.findAllById(
                ArgumentMatchers.anySet()
        )).thenReturn(List.of(militaryStatus));

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(List.of(rule));

        SupportSearchRequest request = baseRequest();
        request.setMilitaryStatusId(militaryStatusId);

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );

        assertEquals(
                measure.getSupportId(),
                response.getMeasures()
                        .getFirst()
                        .getSupportId()
        );

        assertEquals(
                "Мера для мобилизованных",
                response.getMeasures()
                        .getFirst()
                        .getName()
        );
    }

    @Test
    void search_shouldReturnMeasure_whenInRuleMatches() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера для участников",
                new BigDecimal("5000.00")
        );

        DictionaryValue militaryStatus = dictionary(
                militaryStatusId,
                "SVO"
        );

        SupportRule rule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "IN"
                ),
                "military_status_id",
                "REFERENCE",
                null,
                null,
                "MOBILIZED,SVO",
                1,
                true,
                null
        );

        stubBaseDependencies(region, measure);

        when(dictionaryValueRepository.findAllById(
                ArgumentMatchers.anySet()
        )).thenReturn(List.of(militaryStatus));

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(List.of(rule));

        SupportSearchRequest request = baseRequest();
        request.setMilitaryStatusId(militaryStatusId);

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );

        assertEquals(
                new BigDecimal("5000.00"),
                response.getMeasures()
                        .getFirst()
                        .getAmount()
        );
    }

    @Test
    void search_shouldReturnMeasure_whenBetweenRuleMatchesChildGrade() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Школьное питание",
                null
        );

        UserChildRequest child = new UserChildRequest();

        child.setBirthDate(
                LocalDate.of(2015, 5, 10)
        );

        child.setEducationLevelId(
                educationLevelId
        );

        child.setGrade(
                (short) 5
        );

        DictionaryValue educationLevel = dictionary(
                educationLevelId,
                "SCHOOL"
        );

        SupportRule educationRule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "child.education_level_id",
                "REFERENCE",
                null,
                null,
                "SCHOOL",
                1,
                true,
                null
        );

        SupportRule gradeRule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "BETWEEN"
                ),
                "child.grade",
                "NUMBER",
                "1",
                "11",
                null,
                1,
                true,
                null
        );

        stubBaseDependencies(region, measure);

        when(dictionaryValueRepository.findAllById(
                ArgumentMatchers.anySet()
        )).thenReturn(List.of(educationLevel));

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(
                        List.of(
                                educationRule,
                                gradeRule
                        )
                );

        SupportSearchRequest request = baseRequest();

        request.setChildren(
                List.of(child)
        );

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );

        assertEquals(
                "Школьное питание",
                response.getMeasures()
                        .getFirst()
                        .getName()
        );
    }

    @Test
    void search_shouldReturnMeasure_whenGteRuleMatches() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера по возрасту",
                new BigDecimal("3000.00")
        );

        SupportRule rule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "GTE"
                ),
                "child.grade",
                "NUMBER",
                "5",
                null,
                null,
                1,
                true,
                null
        );

        UserChildRequest child = new UserChildRequest();

        child.setBirthDate(
                LocalDate.of(2012, 1, 1)
        );

        child.setGrade(
                (short) 7
        );

        stubBaseDependencies(region, measure);

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(List.of(rule));

        SupportSearchRequest request = baseRequest();

        request.setChildren(
                List.of(child)
        );

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );
    }

    @Test
    void search_shouldReturnMeasure_whenAllRulesInGroupMatch() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера с AND-условиями",
                new BigDecimal("7000.00")
        );

        SupportRule injuryRule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "injury",
                "BOOLEAN",
                null,
                null,
                "true",
                1,
                true,
                null
        );

        SupportRule disabilityRule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "disability",
                "BOOLEAN",
                null,
                null,
                "true",
                1,
                true,
                null
        );

        stubBaseDependencies(region, measure);

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(
                        List.of(
                                injuryRule,
                                disabilityRule
                        )
                );

        SupportSearchRequest request = baseRequest();

        request.setInjury(true);
        request.setDisability(true);

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );
    }

    @Test
    void search_shouldNotReturnMeasure_whenOneAndConditionFails() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера с AND-условиями",
                new BigDecimal("7000.00")
        );

        SupportRule injuryRule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "injury",
                "BOOLEAN",
                null,
                null,
                "true",
                1,
                true,
                null
        );

        SupportRule disabilityRule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "disability",
                "BOOLEAN",
                null,
                null,
                "true",
                1,
                true,
                null
        );

        stubBaseDependencies(region, measure);

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(
                        List.of(
                                injuryRule,
                                disabilityRule
                        )
                );

        SupportSearchRequest request = baseRequest();

        request.setInjury(true);
        request.setDisability(false);

        SupportSearchResponse response =
                service.search(request);

        assertTrue(
                response.getMeasures().isEmpty()
        );
    }

    @Test
    void search_shouldReturnMeasure_whenSecondOrGroupMatches() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера с альтернативными условиями",
                new BigDecimal("5000.00")
        );

        SupportRule firstGroup = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "injury",
                "BOOLEAN",
                null,
                null,
                "true",
                1,
                true,
                null
        );

        SupportRule secondGroup = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "housing_problem",
                "BOOLEAN",
                null,
                null,
                "true",
                2,
                true,
                null
        );

        stubBaseDependencies(region, measure);

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(
                        List.of(
                                firstGroup,
                                secondGroup
                        )
                );

        SupportSearchRequest request = baseRequest();

        request.setInjury(false);
        request.setHousingProblem(true);

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );
    }

    @Test
    void search_shouldUseAmountOverride_whenGroupMatches() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Компенсация",
                new BigDecimal("1000.00")
        );

        SupportRule rule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "injury",
                "BOOLEAN",
                null,
                null,
                "true",
                1,
                true,
                new BigDecimal("15000.00")
        );

        stubBaseDependencies(region, measure);

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(List.of(rule));

        SupportSearchRequest request = baseRequest();

        request.setInjury(true);

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );

        assertEquals(
                new BigDecimal("15000.00"),
                response.getMeasures()
                        .getFirst()
                        .getAmount()
        );
    }

    @Test
    void search_shouldIgnoreNonRequiredRule_whenRequiredConditionsMatch() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера с необязательным условием",
                new BigDecimal("3000.00")
        );

        SupportRule optionalRule = rule(
                measure,
                dictionary(
                        UUID.randomUUID(),
                        "EQUALS"
                ),
                "injury",
                "BOOLEAN",
                null,
                null,
                "true",
                1,
                false,
                null
        );

        stubBaseDependencies(region, measure);

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(List.of(optionalRule));

        SupportSearchRequest request = baseRequest();

        request.setInjury(false);

        SupportSearchResponse response =
                service.search(request);

        assertEquals(
                1,
                response.getMeasures().size()
        );
    }

    @Test
    void search_shouldReturnMeasureWithoutRules() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Мера без правил",
                new BigDecimal("2000.00")
        );

        stubBaseDependencies(region, measure);

        when(supportRuleRepository
                .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                        measure.getSupportId()
                ))
                .thenReturn(List.of());

        SupportSearchResponse response =
                service.search(baseRequest());

        assertEquals(
                1,
                response.getMeasures().size()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                response.getMeasures()
                        .getFirst()
                        .getAmount()
        );
    }

    @Test
    void search_shouldNotReturnExpiredMeasure() {
        Region region = createRegion();

        SupportMeasure measure = createMeasure(
                "Истёкшая мера",
                new BigDecimal("2000.00")
        );

        measure.setValidFrom(
                LocalDate.of(2025, 1, 1)
        );

        measure.setValidTo(
                LocalDate.of(2026, 1, 1)
        );

        stubBaseDependencies(region, measure);

        SupportSearchResponse response =
                service.search(baseRequest());

        assertTrue(
                response.getMeasures().isEmpty()
        );
    }

    @Test
    void search_shouldThrowBadRequest_whenRequestIsNull() {
        assertThrows(
                BadRequestException.class,
                () -> service.search(null)
        );
    }

    @Test
    void search_shouldThrowBadRequest_whenRegionIsMissing() {
        SupportSearchRequest request =
                baseRequest();

        request.setRegionId(null);

        assertThrows(
                BadRequestException.class,
                () -> service.search(request)
        );
    }

    @Test
    void search_shouldThrowBadRequest_whenMunicipalityIsMissing() {
        SupportSearchRequest request =
                baseRequest();

        request.setMunicipalityId(null);

        assertThrows(
                BadRequestException.class,
                () -> service.search(request)
        );
    }

    private void stubBaseDependencies(
            Region region,
            SupportMeasure measure
    ) {
        when(regionRepository.findById(regionId))
                .thenReturn(Optional.of(region));

        when(supportMunicipalityRepository
                .findAllById_MunicipalityId(municipalityId))
                .thenReturn(List.of());

        when(supportMeasureRepository.findAll())
                .thenReturn(List.of(measure));
    }

    private SupportSearchRequest baseRequest() {
        SupportSearchRequest request =
                new SupportSearchRequest();

        request.setRegionId(regionId);
        request.setMunicipalityId(municipalityId);

        request.setInjury(false);
        request.setDisability(false);
        request.setHousingProblem(false);
        request.setGasificationNeeded(false);

        request.setChildren(List.of());

        return request;
    }

    private Region createRegion() {
        Region region = new Region();

        region.setRegionId(regionId);
        region.setName("Татарстан");
        region.setCode("TATARSTAN");

        return region;
    }

    private SupportMeasure createMeasure(
            String name,
            BigDecimal amount
    ) {
        SupportMeasure measure =
                new SupportMeasure();

        measure.setSupportId(
                UUID.randomUUID()
        );

        measure.setName(name);

        measure.setDescription(
                "Описание меры"
        );

        measure.setAmount(amount);

        measure.setApplicationRequired(true);

        measure.setActionUrl(
                "https://example.com"
        );

        measure.setLevel(
                dictionary(
                        UUID.randomUUID(),
                        "FEDERAL"
                )
        );

        return measure;
    }

    private SupportRule rule(
            SupportMeasure measure,
            DictionaryValue operator,
            String parameter,
            String valueType,
            String valueFrom,
            String valueTo,
            String value,
            Integer conditionGroup,
            boolean required,
            BigDecimal amountOverride
    ) {
        SupportRule rule =
                new SupportRule();

        rule.setRuleId(
                UUID.randomUUID()
        );

        rule.setSupport(
                measure
        );

        rule.setOperator(
                operator
        );

        rule.setParameter(
                parameter
        );

        rule.setValueType(
                valueType
        );

        rule.setValueFrom(
                valueFrom
        );

        rule.setValueTo(
                valueTo
        );

        rule.setValue(
                value
        );

        rule.setConditionGroup(
                conditionGroup
        );

        rule.setRequired(
                required
        );

        rule.setAmountOverride(
                amountOverride
        );

        return rule;
    }

    private DictionaryValue dictionary(
            UUID id,
            String code
    ) {
        DictionaryValue value =
                new DictionaryValue();

        value.setDictionaryValueId(id);
        value.setCode(code);

        return value;
    }
}