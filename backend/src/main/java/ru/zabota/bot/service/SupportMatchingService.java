package ru.zabota.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.support.SupportMeasureShortResponse;
import ru.zabota.bot.dto.support.SupportSearchRequest;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.entity.SupportMunicipality;
import ru.zabota.bot.entity.SupportRule;
import ru.zabota.bot.exception.BadRequestException;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.RegionRepository;
import ru.zabota.bot.repository.SupportMeasureRepository;
import ru.zabota.bot.repository.SupportMunicipalityRepository;
import ru.zabota.bot.repository.SupportRuleRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * Сервис персонального подбора мер социальной поддержки.
 *
 * Получает входные параметры пользователя, определяет доступные
 * на его территории меры поддержки и проверяет машиночитаемые
 * правила из support_rule.
 *
 * Логика правил:
 *
 * - одинаковый condition_group -> AND;
 * - разные condition_group -> OR;
 * - isRequired = false не блокирует совпадение;
 * - amountOverride переопределяет базовую сумму меры;
 * - условия child.* проверяются относительно детей пользователя;
 * - для child-условий группа считается выполненной, если существует
 *   хотя бы один ребёнок, удовлетворяющий всем обязательным условиям
 *   этой группы.
 *
 * REFERENCE-значения сравниваются по машинному коду DictionaryValue.
 *
 * Сервис не сохраняет пользователя и user_support.
 * Он только рассчитывает результат подбора.
 */
@Service
@Transactional(readOnly = true)
public class SupportMatchingService {

    private static final String VALUE_TYPE_NUMBER = "NUMBER";
    private static final String VALUE_TYPE_DATE = "DATE";
    private static final String VALUE_TYPE_TEXT = "TEXT";
    private static final String VALUE_TYPE_BOOLEAN = "BOOLEAN";
    private static final String VALUE_TYPE_REFERENCE = "REFERENCE";

    private static final String OP_EQUALS = "EQUALS";
    private static final String OP_NOT_EQUALS = "NOT_EQUALS";
    private static final String OP_IN = "IN";
    private static final String OP_NOT_IN = "NOT_IN";
    private static final String OP_BETWEEN = "BETWEEN";
    private static final String OP_GTE = "GTE";
    private static final String OP_LTE = "LTE";
    private static final String OP_GT = "GT";
    private static final String OP_LT = "LT";

    private final SupportMeasureRepository supportMeasureRepository;
    private final SupportMunicipalityRepository supportMunicipalityRepository;
    private final SupportRuleRepository supportRuleRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final RegionRepository regionRepository;
    private final Clock clock;

    @Autowired
    public SupportMatchingService(
            SupportMeasureRepository supportMeasureRepository,
            SupportMunicipalityRepository supportMunicipalityRepository,
            SupportRuleRepository supportRuleRepository,
            DictionaryValueRepository dictionaryValueRepository,
            RegionRepository regionRepository
    ) {
        this(
                supportMeasureRepository,
                supportMunicipalityRepository,
                supportRuleRepository,
                dictionaryValueRepository,
                regionRepository,
                Clock.systemUTC()
        );
    }

    SupportMatchingService(
            SupportMeasureRepository supportMeasureRepository,
            SupportMunicipalityRepository supportMunicipalityRepository,
            SupportRuleRepository supportRuleRepository,
            DictionaryValueRepository dictionaryValueRepository,
            RegionRepository regionRepository,
            Clock clock
    ) {
        this.supportMeasureRepository = supportMeasureRepository;
        this.supportMunicipalityRepository = supportMunicipalityRepository;
        this.supportRuleRepository = supportRuleRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.regionRepository = regionRepository;
        this.clock = clock;
    }

    public SupportSearchResponse search(SupportSearchRequest request) {
        validateRequest(request);

        MatchingContext context = buildContext(request);
        Set<UUID> activeMunicipalitySupportIds =
                getActiveMunicipalitySupportIds(request.getMunicipalityId());

        List<SupportMeasureShortResponse> measures =
                supportMeasureRepository.findAll()
                        .stream()
                        .filter(measure ->
                                isMeasureAvailable(
                                        measure,
                                        context,
                                        activeMunicipalitySupportIds
                                )
                        )
                        .map(measure ->
                                matchMeasure(measure, context)
                        )
                        .filter(MatchResult::matched)
                        .map(result ->
                                toResponse(
                                        result.measure(),
                                        result.amount()
                                )
                        )
                        .toList();

        SupportSearchResponse response = new SupportSearchResponse();
        response.setMeasures(measures);

        return response;
    }

    private void validateRequest(SupportSearchRequest request) {
        if (request == null) {
            throw new BadRequestException(
                    "Запрос на подбор не может быть пустым"
            );
        }

        if (request.getRegionId() == null) {
            throw new BadRequestException(
                    "Регион обязателен"
            );
        }

        if (request.getMunicipalityId() == null) {
            throw new BadRequestException(
                    "Муниципалитет обязателен"
            );
        }
    }

    private MatchingContext buildContext(
            SupportSearchRequest request
    ) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Регион с id "
                                        + request.getRegionId()
                                        + " не найден"
                        )
                );

        Map<UUID, String> dictionaryCodes =
                loadDictionaryCodes(request);

        return new MatchingContext(
                request,
                region,
                dictionaryCodes,
                LocalDate.now(clock)
        );
    }

    private Map<UUID, String> loadDictionaryCodes(
            SupportSearchRequest request
    ) {
        Set<UUID> ids = new HashSet<>();

        addIfPresent(ids, request.getFamilyRelationId());
        addIfPresent(ids, request.getMilitaryStatusId());
        addIfPresent(ids, request.getSexId());
        addIfPresent(ids, request.getDisabilityGroupId());
        addIfPresent(ids, request.getEmploymentStatusId());
        addIfPresent(ids, request.getIncomeCategoryId());
        addIfPresent(ids, request.getLegalIssueCategoryId());

        for (UserChildRequest child : safeChildren(request.getChildren())) {
            addIfPresent(ids, child.getEducationLevelId());
            addIfPresent(ids, child.getDisabilityGroupId());
            addIfPresent(ids, child.getInstitutionTypeId());
        }

        if (ids.isEmpty()) {
            return Map.of();
        }

        return dictionaryValueRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        DictionaryValue::getDictionaryValueId,
                        DictionaryValue::getCode
                ));
    }

    private void addIfPresent(
            Set<UUID> ids,
            UUID id
    ) {
        if (id != null) {
            ids.add(id);
        }
    }

    private List<UserChildRequest> safeChildren(
            List<UserChildRequest> children
    ) {
        return children == null
                ? List.of()
                : children;
    }

    private Set<UUID> getActiveMunicipalitySupportIds(
            UUID municipalityId
    ) {
        LocalDate today = LocalDate.now(clock);

        return supportMunicipalityRepository
                .findAllById_MunicipalityId(municipalityId)
                .stream()
                .filter(link ->
                        isDateActive(
                                link.getValidFrom(),
                                link.getValidTo(),
                                today
                        )
                )
                .map(SupportMunicipality::getSupport)
                .map(SupportMeasure::getSupportId)
                .collect(Collectors.toSet());
    }

    private boolean isMeasureAvailable(
            SupportMeasure measure,
            MatchingContext context,
            Set<UUID> activeMunicipalitySupportIds
    ) {
        if (!isDateActive(
                measure.getValidFrom(),
                measure.getValidTo(),
                context.today()
        )) {
            return false;
        }

        String levelCode = getDictionaryCode(
                measure.getLevel()
        );

        if ("MUNICIPAL".equalsIgnoreCase(levelCode)
                && !activeMunicipalitySupportIds.contains(
                measure.getSupportId()
        )) {
            return false;
        }

        return true;
    }

    private MatchResult matchMeasure(
            SupportMeasure measure,
            MatchingContext context
    ) {
        List<SupportRule> rules =
                supportRuleRepository
                        .findAllBySupport_SupportIdOrderByConditionGroupAsc(
                                measure.getSupportId()
                        );

        if (rules.isEmpty()) {
            return new MatchResult(
                    true,
                    measure,
                    measure.getAmount()
            );
        }

        Map<Integer, List<SupportRule>> groups =
                rules.stream()
                        .collect(Collectors.groupingBy(
                                SupportRule::getConditionGroup
                        ));

        for (List<SupportRule> groupRules : groups.values()) {
            if (matchesGroup(groupRules, context)) {
                BigDecimal amount =
                        resolveAmount(measure, groupRules);

                return new MatchResult(
                        true,
                        measure,
                        amount
                );
            }
        }

        return new MatchResult(
                false,
                measure,
                null
        );
    }

    private boolean matchesGroup(
            List<SupportRule> rules,
            MatchingContext context
    ) {
        List<SupportRule> userRules = rules.stream()
                .filter(rule ->
                        !isChildParameter(rule.getParameter())
                )
                .toList();

        List<SupportRule> childRules = rules.stream()
                .filter(rule ->
                        isChildParameter(rule.getParameter())
                )
                .toList();

        boolean userRulesMatch = userRules.stream()
                .filter(SupportRule::isRequired)
                .allMatch(rule ->
                        matchesUserRule(rule, context)
                );

        if (!userRulesMatch) {
            return false;
        }

        if (childRules.isEmpty()) {
            return true;
        }

        return context.children()
                .stream()
                .anyMatch(child ->
                        childRules.stream()
                                .filter(SupportRule::isRequired)
                                .allMatch(rule ->
                                        matchesChildRule(
                                                rule,
                                                child,
                                                context
                                        )
                                )
                );
    }

    private boolean matchesUserRule(
            SupportRule rule,
            MatchingContext context
    ) {
        Object actualValue =
                resolveUserParameter(
                        rule.getParameter(),
                        context
                );

        return matchesRule(
                rule,
                actualValue,
                context
        );
    }

    private boolean matchesChildRule(
            SupportRule rule,
            UserChildRequest child,
            MatchingContext context
    ) {
        Object actualValue =
                resolveChildParameter(
                        rule.getParameter(),
                        child,
                        context
                );

        return matchesRule(
                rule,
                actualValue,
                context
        );
    }

    private Object resolveUserParameter(
            String parameter,
            MatchingContext context
    ) {
        SupportSearchRequest request =
                context.request();

        return switch (parameter) {
            case "region_id" ->
                    context.region().getCode();

            case "municipality_id" ->
                    request.getMunicipalityId();

            case "family_relation_id" ->
                    context.dictionaryCode(
                            request.getFamilyRelationId()
                    );

            case "military_status_id" ->
                    context.dictionaryCode(
                            request.getMilitaryStatusId()
                    );

            case "birth_date" ->
                    request.getBirthDate();

            case "user.age" ->
                    calculateAge(request.getBirthDate(), context.today());

            case "children.count" ->
                    context.children().size();

            case "pregnancy" ->
                    request.getPregnancy();

            case "pregnancy_days" ->
                    request.getPregnancyDays();

            case "sex_id" ->
                    context.dictionaryCode(request.getSexId());

            case "injury" ->
                    request.isInjury();

            case "disability" ->
                    request.isDisability();

            case "disability_group_id" ->
                    context.dictionaryCode(
                            request.getDisabilityGroupId()
                    );

            case "housing_problem" ->
                    request.isHousingProblem();

            case "gasification_needed" ->
                    request.isGasificationNeeded();

            case "employment_status_id" ->
                    context.dictionaryCode(
                            request.getEmploymentStatusId()
                    );

            case "income_category_id" ->
                    context.dictionaryCode(
                            request.getIncomeCategoryId()
                    );

            case "loan_exists" ->
                    request.isLoanExists();

            case "business_plan" ->
                    request.isBusinessPlan();

            case "job_seeker" ->
                    request.isJobSeeker();

            case "social_service_need" ->
                    request.isSocialServiceNeed();

            case "serviceman_leave_start" ->
                    request.getServicemanLeaveStart();

            case "serviceman_leave_end" ->
                    request.getServicemanLeaveEnd();

            case "serviceman_on_leave" ->
                    isDateInRange(
                            request.getServicemanLeaveStart(),
                            request.getServicemanLeaveEnd(),
                            context.today()
                    );

            case "legal_issue_category_id" ->
                    context.dictionaryCode(request.getLegalIssueCategoryId());

            default ->
                    throw new BadRequestException(
                            "Неизвестный параметр правила: "
                                    + parameter
                    );
        };
    }

    private Object resolveChildParameter(
            String parameter,
            UserChildRequest child,
            MatchingContext context
    ) {
        return switch (parameter) {
            case "child.birth_date", "child_birth_date" ->
                    child.getBirthDate();

            case "child.age" ->
                    calculateAge(
                            child.getBirthDate(),
                            context.today()
                    );

            case "child.education_level_id" ->
                    context.dictionaryCode(
                            child.getEducationLevelId()
                    );

            case "child.grade" ->
                    child.getGrade();

            case "child.disability" ->
                    child.isDisability();

            case "child.disability_group_id" ->
                    context.dictionaryCode(
                            child.getDisabilityGroupId()
                    );

            case "child.full_time" ->
                    child.isFullTime();

            case "child.institution_type_id" ->
                    context.dictionaryCode(child.getInstitutionTypeId());

            default ->
                    throw new BadRequestException(
                            "Неизвестный параметр правила ребёнка: "
                                    + parameter
                    );
        };
    }

    private int calculateAge(
            LocalDate birthDate,
            LocalDate today
    ) {
        if (birthDate == null) {
            return -1;
        }

        if (birthDate.isAfter(today)) {
            return -1;
        }

        return Period.between(
                birthDate,
                today
        ).getYears();
    }

    private boolean matchesRule(
            SupportRule rule,
            Object actualValue,
            MatchingContext context
    ) {
        if (actualValue == null) {
            return false;
        }

        String operator = getDictionaryCode(
                rule.getOperator()
        );

        if (operator == null) {
            throw new BadRequestException(
                    "У правила "
                            + rule.getRuleId()
                            + " не определён оператор"
            );
        }

        return switch (operator.toUpperCase()) {
            case OP_EQUALS ->
                    matchesEquals(
                            rule,
                            actualValue,
                            context
                    );

            case OP_NOT_EQUALS ->
                    !matchesEquals(
                            rule,
                            actualValue,
                            context
                    );

            case OP_IN ->
                    matchesIn(
                            rule,
                            actualValue,
                            context
                    );

            case OP_NOT_IN ->
                    !matchesIn(
                            rule,
                            actualValue,
                            context
                    );

            case OP_BETWEEN ->
                    matchesBetween(
                            rule,
                            actualValue
                    );

            case OP_GTE ->
                    matchesComparisonBoundary(
                            rule,
                            actualValue,
                            true,
                            true
                    );

            case OP_LTE ->
                    matchesComparisonBoundary(
                            rule,
                            actualValue,
                            false,
                            true
                    );

            case OP_GT ->
                    matchesComparisonBoundary(
                            rule,
                            actualValue,
                            true,
                            false
                    );

            case OP_LT ->
                    matchesComparisonBoundary(
                            rule,
                            actualValue,
                            false,
                            false
                    );

            default ->
                    throw new BadRequestException(
                            "Неподдерживаемый оператор правила: "
                                    + operator
                    );
        };
    }

    private boolean matchesEquals(
            SupportRule rule,
            Object actualValue,
            MatchingContext context
    ) {
        Object expectedValue =
                parseValue(
                        rule.getValue(),
                        rule.getValueType(),
                        context
                );

        return compareValues(
                actualValue,
                expectedValue
        ) == 0;
    }

    private boolean matchesIn(
            SupportRule rule,
            Object actualValue,
            MatchingContext context
    ) {
        if (rule.getValue() == null) {
            return false;
        }

        return java.util.Arrays.stream(
                        rule.getValue().split(",")
                )
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(value ->
                        parseValue(
                                value,
                                rule.getValueType(),
                                context
                        )
                )
                .anyMatch(expected ->
                        compareValues(
                                actualValue,
                                expected
                        ) == 0
                );
    }

    private boolean matchesBetween(
            SupportRule rule,
            Object actualValue
    ) {
        Object from =
                parseTypedValue(
                        rule.getValueFrom(),
                        rule.getValueType()
                );

        Object to =
                parseTypedValue(
                        rule.getValueTo(),
                        rule.getValueType()
                );

        if (from == null || to == null) {
            return false;
        }

        return compareValues(
                actualValue,
                from
        ) >= 0
                && compareValues(
                actualValue,
                to
        ) <= 0;
    }

    private boolean matchesComparisonBoundary(
            SupportRule rule,
            Object actualValue,
            boolean greater,
            boolean inclusive
    ) {
        String boundaryValue = greater
                ? firstNotNull(
                rule.getValueFrom(),
                rule.getValue()
        )
                : firstNotNull(
                rule.getValueTo(),
                rule.getValue()
        );

        Object expected =
                parseTypedValue(
                        boundaryValue,
                        rule.getValueType()
                );

        if (expected == null) {
            return false;
        }

        int comparison =
                compareValues(
                        actualValue,
                        expected
                );

        if (greater) {
            return inclusive
                    ? comparison >= 0
                    : comparison > 0;
        }

        return inclusive
                ? comparison <= 0
                : comparison < 0;
    }

    private String firstNotNull(
            String first,
            String second
    ) {
        return first != null
                ? first
                : second;
    }

    private Object parseValue(
            String value,
            String valueType,
            MatchingContext context
    ) {
        return parseTypedValue(
                value,
                valueType
        );
    }

    private Object parseTypedValue(
            String value,
            String valueType
    ) {
        if (value == null) {
            return null;
        }

        if (valueType == null) {
            throw new BadRequestException(
                    "У правила не указан value_type"
            );
        }

        return switch (valueType.toUpperCase()) {
            case VALUE_TYPE_NUMBER ->
                    new BigDecimal(value.trim());

            case VALUE_TYPE_DATE ->
                    LocalDate.parse(value.trim());

            case VALUE_TYPE_BOOLEAN ->
                    parseBoolean(value);

            case VALUE_TYPE_TEXT,
                 VALUE_TYPE_REFERENCE ->
                    value.trim();

            default ->
                    throw new BadRequestException(
                            "Неподдерживаемый value_type: "
                                    + valueType
                    );
        };
    }

    private Boolean parseBoolean(String value) {
        if ("true".equalsIgnoreCase(value.trim())) {
            return true;
        }

        if ("false".equalsIgnoreCase(value.trim())) {
            return false;
        }

        throw new BadRequestException(
                "Некорректное BOOLEAN-значение: "
                        + value
        );
    }

    private int compareValues(
            Object actual,
            Object expected
    ) {
        if (actual == null || expected == null) {
            return -1;
        }

        if (actual instanceof Number actualNumber
                && expected instanceof Number expectedNumber) {
            return new BigDecimal(
                    actualNumber.toString()
            ).compareTo(
                    new BigDecimal(
                            expectedNumber.toString()
                    )
            );
        }

        if (actual instanceof Boolean actualBoolean
                && expected instanceof Boolean expectedBoolean) {
            return Boolean.compare(
                    actualBoolean,
                    expectedBoolean
            );
        }

        if (actual instanceof UUID actualId
                && expected instanceof String expectedString) {
            return actualId.toString()
                    .equalsIgnoreCase(expectedString)
                    ? 0
                    : -1;
        }

        if (actual instanceof UUID actualId
                && expected instanceof UUID expectedId) {
            return actualId.compareTo(expectedId);
        }

        if (actual instanceof String actualString
                && expected instanceof String expectedString) {
            return actualString.compareToIgnoreCase(
                    expectedString
            );
        }

        if (actual instanceof Comparable<?> comparable
                && expected.getClass().equals(
                actual.getClass()
        )) {
            @SuppressWarnings("unchecked")
            Comparable<Object> value =
                    (Comparable<Object>) comparable;

            return value.compareTo(expected);
        }

        return actual.equals(expected)
                ? 0
                : -1;
    }

    private boolean isChildParameter(String parameter) {
        return parameter != null
                && (
                parameter.startsWith("child.")
                        || parameter.startsWith("child_")
        );
    }

    private boolean isDateInRange(
            LocalDate start,
            LocalDate end,
            LocalDate date
    ) {
        return start != null
                && end != null
                && !date.isBefore(start)
                && !date.isAfter(end);
    }

    private boolean isDateActive(
            LocalDate validFrom,
            LocalDate validTo,
            LocalDate today
    ) {
        boolean started =
                validFrom == null
                        || !today.isBefore(validFrom);

        boolean notFinished =
                validTo == null
                        || !today.isAfter(validTo);

        return started && notFinished;
    }

    private BigDecimal resolveAmount(
            SupportMeasure measure,
            List<SupportRule> groupRules
    ) {
        return groupRules.stream()
                .map(SupportRule::getAmountOverride)
                .filter(value -> value != null)
                .findFirst()
                .orElse(measure.getAmount());
    }

    private String getDictionaryCode(
            DictionaryValue value
    ) {
        return value == null
                ? null
                : value.getCode();
    }

    private SupportMeasureShortResponse toResponse(
            SupportMeasure measure,
            BigDecimal amount
    ) {
        SupportMeasureShortResponse response =
                new SupportMeasureShortResponse();

        response.setSupportId(
                measure.getSupportId()
        );
        response.setName(
                measure.getName()
        );
        response.setDescription(
                measure.getDescription()
        );
        response.setAmount(amount);
        response.setFrequency(
                getDictionaryCode(
                        measure.getFrequency()
                )
        );
        response.setApplicationRequired(
                measure.isApplicationRequired()
        );
        response.setApplicationChannel(
                getDictionaryCode(
                        measure.getApplicationChannel()
                )
        );
        response.setActionUrl(
                measure.getActionUrl()
        );

        return response;
    }

    private record MatchingContext(
            SupportSearchRequest request,
            Region region,
            Map<UUID, String> dictionaryCodes,
            LocalDate today
    ) {

        List<UserChildRequest> children() {
            return request.getChildren() == null
                    ? List.of()
                    : request.getChildren();
        }

        String dictionaryCode(UUID id) {
            return id == null
                    ? null
                    : dictionaryCodes.get(id);
        }
    }

    private record MatchResult(
            boolean matched,
            SupportMeasure measure,
            BigDecimal amount
    ) {
    }
}