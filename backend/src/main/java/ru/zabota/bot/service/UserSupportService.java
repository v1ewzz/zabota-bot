package ru.zabota.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.support.SupportSearchRequest;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.dto.support.SupportMeasureShortResponse;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;
import ru.zabota.bot.dto.usersupport.UserSupportUpdateRequest;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserChild;
import ru.zabota.bot.entity.UserSupport;
import ru.zabota.bot.exception.BadRequestException;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.UserSupportMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.SupportMeasureRepository;
import ru.zabota.bot.repository.UserChildRepository;
import ru.zabota.bot.repository.UserRepository;
import ru.zabota.bot.repository.UserSupportRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * Сервис персональных результатов подбора мер.
 *
 * Выполняет сквозной сценарий:
 *
 * 1. Загружает сохранённый профиль пользователя.
 * 2. Преобразует его в SupportSearchRequest.
 * 3. Передаёт данные в существующий rules engine.
 * 4. Сохраняет найденные меры в user_support.
 * 5. Не сбрасывает уже установленный пользователем статус,
 *    выбранность и заметку при повторном подборе.
 * 6. Предоставляет операции личного кабинета и изменения статуса.
 */
@Service
public class UserSupportService {

    private static final String USER_SUPPORT_STATUS = "USER_SUPPORT_STATUS";
    private static final String STATUS_NOT_APPLIED = "NOT_APPLIED";
    private static final String STATUS_SUBMITTED = "SUBMITTED";
    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_RECEIVED = "RECEIVED";

    private final UserRepository userRepository;
    private final UserChildRepository userChildRepository;
    private final UserSupportRepository userSupportRepository;
    private final SupportMeasureRepository supportMeasureRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final SupportMatchingService supportMatchingService;
    private final UserSupportMapper userSupportMapper;
    private final Clock clock;

    @Autowired
    public UserSupportService(
            UserRepository userRepository,
            UserChildRepository userChildRepository,
            UserSupportRepository userSupportRepository,
            SupportMeasureRepository supportMeasureRepository,
            DictionaryValueRepository dictionaryValueRepository,
            SupportMatchingService supportMatchingService,
            UserSupportMapper userSupportMapper
    ) {
        this(
                userRepository,
                userChildRepository,
                userSupportRepository,
                supportMeasureRepository,
                dictionaryValueRepository,
                supportMatchingService,
                userSupportMapper,
                Clock.systemUTC()
        );
    }

    UserSupportService(
            UserRepository userRepository,
            UserChildRepository userChildRepository,
            UserSupportRepository userSupportRepository,
            SupportMeasureRepository supportMeasureRepository,
            DictionaryValueRepository dictionaryValueRepository,
            SupportMatchingService supportMatchingService,
            UserSupportMapper userSupportMapper,
            Clock clock
    ) {
        this.userRepository = userRepository;
        this.userChildRepository = userChildRepository;
        this.userSupportRepository = userSupportRepository;
        this.supportMeasureRepository = supportMeasureRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.supportMatchingService = supportMatchingService;
        this.userSupportMapper = userSupportMapper;
        this.clock = clock;
    }

    @Transactional
    public SupportSearchResponse rematch(UUID userId) {
        User user = getUser(userId);
        validateProfileForMatching(user);
        SupportSearchRequest request = buildSearchRequest(userId, user);

        SupportSearchResponse searchResponse =
                supportMatchingService.search(request);

        searchResponse.setUserId(userId);

        synchronizeResults(
                user,
                searchResponse.getMeasures()
        );

        return searchResponse;
    }

    @Transactional(readOnly = true)
    public List<UserSupportResponse> getAll(UUID userId) {
        ensureUserExists(userId);

        return userSupportRepository
                .findAllByUser_UserIdOrderByCheckedAtDesc(userId)
                .stream()
                .map(userSupportMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserSupportResponse getById(
            UUID userId,
            UUID userSupportId
    ) {
        UserSupport userSupport = getUserSupport(
                userId,
                userSupportId
        );

        return userSupportMapper.toResponse(userSupport);
    }

    @Transactional
    public UserSupportResponse update(
            UUID userId,
            UUID userSupportId,
            UserSupportUpdateRequest request
    ) {
        if (request == null) {
            throw new BadRequestException(
                    "Запрос на изменение меры не может быть пустым"
            );
        }

        if (request.getStatusId() == null
                && request.getSelectedForAction() == null
                && request.getNote() == null) {
            throw new BadRequestException(
                    "Не указано ни одного изменения"
            );
        }

        UserSupport userSupport = getUserSupport(
                userId,
                userSupportId
        );

        if (request.getStatusId() != null) {
            DictionaryValue status = getActiveDictionaryValueById(
                    request.getStatusId(),
                    USER_SUPPORT_STATUS,
                    "Статус оформления меры"
            );

            userSupport.setStatus(status);
            applyStatusTimestamp(userSupport, status.getCode());
        }

        if (request.getSelectedForAction() != null) {
            userSupport.setSelectedForAction(
                    request.getSelectedForAction()
            );
        }

        if (request.getNote() != null) {
            String note = request.getNote().trim();
            userSupport.setNote(note.isEmpty() ? null : note);
        }

        return userSupportMapper.toResponse(
                userSupportRepository.save(userSupport)
        );
    }

    private void synchronizeResults(
            User user,
            List<SupportMeasureShortResponse> matchedMeasures
    ) {
        Map<UUID, UserSupport> existingBySupportId =
                new HashMap<>();

        userSupportRepository
                .findAllByUser_UserIdOrderByCheckedAtDesc(
                        user.getUserId()
                )
                .forEach(item ->
                        existingBySupportId.put(
                                item.getSupport().getSupportId(),
                                item
                        )
                );

        DictionaryValue defaultStatus =
                getActiveDictionaryValueByCode(
                        USER_SUPPORT_STATUS,
                        STATUS_NOT_APPLIED,
                        "Начальный статус оформления меры"
                );

        for (SupportMeasureShortResponse matchedMeasure : matchedMeasures) {
            SupportMeasure support = supportMeasureRepository
                    .findById(matchedMeasure.getSupportId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Мера поддержки с id "
                                            + matchedMeasure.getSupportId()
                                            + " не найдена"
                            )
                    );

            UserSupport userSupport = existingBySupportId.get(
                    support.getSupportId()
            );

            if (userSupport == null) {
                userSupport = new UserSupport();
                userSupport.setUser(user);
                userSupport.setSupport(support);
                userSupport.setStatus(defaultStatus);
                userSupport.setSelectedForAction(false);
            }

            userSupport.setCheckedAt(
                    LocalDateTime.now(clock)
            );
            userSupport.setMatchedAmount(
                    matchedMeasure.getAmount()
            );

            userSupportRepository.save(userSupport);
        }

        removeStaleNotAppliedResults(
                existingBySupportId,
                matchedMeasures
        );
    }

    private void removeStaleNotAppliedResults(
            Map<UUID, UserSupport> existingBySupportId,
            List<SupportMeasureShortResponse> matchedMeasures
    ) {
        java.util.Set<UUID> matchedIds = matchedMeasures
                .stream()
                .map(SupportMeasureShortResponse::getSupportId)
                .collect(java.util.stream.Collectors.toSet());

        existingBySupportId.values()
                .stream()
                .filter(item ->
                        item.getStatus() != null
                                && STATUS_NOT_APPLIED.equals(
                                item.getStatus().getCode()
                        )
                )
                .filter(item ->
                        !matchedIds.contains(
                                item.getSupport().getSupportId()
                        )
                )
                .forEach(userSupportRepository::delete);
    }

    private void validateProfileForMatching(User user) {
        if (user.getRegion() == null
                || user.getMunicipality() == null) {
            throw new BadRequestException(
                    "Профиль пользователя заполнен не полностью: регион и муниципалитет обязательны"
            );
        }

        if (user.getFamilyRelation() == null) {
            throw new BadRequestException(
                    "Профиль пользователя не содержит семейную роль"
            );
        }

        if (user.getMilitaryStatus() == null) {
            throw new BadRequestException(
                    "Профиль пользователя не содержит статус военнослужащего"
            );
        }
    }

    private SupportSearchRequest buildSearchRequest(
            UUID userId,
            User user
    ) {
        SupportSearchRequest request = new SupportSearchRequest();

        request.setRegionId(
                user.getRegion().getRegionId()
        );
        request.setMunicipalityId(
                user.getMunicipality().getMunicipalityId()
        );

        if (user.getFamilyRelation() != null) {
            request.setFamilyRelationId(
                    user.getFamilyRelation().getDictionaryValueId()
            );
        }

        if (user.getMilitaryStatus() != null) {
            request.setMilitaryStatusId(
                    user.getMilitaryStatus().getDictionaryValueId()
            );
        }

        request.setPregnancy(user.getPregnancy());
        request.setInjury(user.isInjury());
        request.setDisability(user.isDisability());

        if (user.getDisabilityGroup() != null) {
            request.setDisabilityGroupId(
                    user.getDisabilityGroup().getDictionaryValueId()
            );
        }

        request.setHousingProblem(user.isHousingProblem());
        request.setGasificationNeeded(user.isGasificationNeeded());

        if (user.getEmploymentStatus() != null) {
            request.setEmploymentStatusId(
                    user.getEmploymentStatus().getDictionaryValueId()
            );
        }

        if (user.getIncomeCategory() != null) {
            request.setIncomeCategoryId(
                    user.getIncomeCategory().getDictionaryValueId()
            );
        }

        List<UserChildRequest> children = userChildRepository
                .findAllByUser_UserId(userId)
                .stream()
                .map(this::toChildRequest)
                .toList();

        request.setChildren(children);

        return request;
    }

    private UserChildRequest toChildRequest(UserChild child) {
        UserChildRequest request = new UserChildRequest();

        request.setBirthDate(child.getBirthDate());
        request.setEducationLevelId(
                child.getEducationLevel().getDictionaryValueId()
        );
        request.setGrade(child.getGrade());
        request.setDisability(child.isDisability());
        request.setFullTime(child.isFullTime());

        if (child.getDisabilityGroup() != null) {
            request.setDisabilityGroupId(
                    child.getDisabilityGroup().getDictionaryValueId()
            );
        }

        return request;
    }

    private DictionaryValue getActiveDictionaryValueById(
            UUID id,
            String expectedType,
            String fieldName
    ) {
        DictionaryValue value = dictionaryValueRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                fieldName + " с id " + id + " не найден"
                        )
                );

        if (!value.isActive()) {
            throw new BadRequestException(
                    fieldName + " недоступен для выбора"
            );
        }

        if (value.getDictionaryType() != null
                && !expectedType.equals(
                value.getDictionaryType().getCode()
        )) {
            throw new BadRequestException(
                    fieldName + " принадлежит другому справочнику"
            );
        }

        return value;
    }

    private DictionaryValue getActiveDictionaryValueByCode(
            String dictionaryType,
            String code,
            String fieldName
    ) {
        DictionaryValue value = dictionaryValueRepository
                .findByDictionaryType_CodeAndCode(
                        dictionaryType,
                        code
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                fieldName
                                        + " с кодом "
                                        + code
                                        + " не найден"
                        )
                );

        if (!value.isActive()) {
            throw new ResourceNotFoundException(
                    fieldName
                            + " с кодом "
                            + code
                            + " недоступен"
            );
        }

        return value;
    }

    private void applyStatusTimestamp(
            UserSupport userSupport,
            String statusCode
    ) {
        LocalDateTime now = LocalDateTime.now(clock);

        if ((STATUS_SUBMITTED.equals(statusCode)
                || STATUS_APPROVED.equals(statusCode)
                || STATUS_RECEIVED.equals(statusCode))
                && userSupport.getSubmittedAt() == null) {
            userSupport.setSubmittedAt(now);
        }

        if (STATUS_RECEIVED.equals(statusCode)
                && userSupport.getReceivedAt() == null) {
            userSupport.setReceivedAt(now);
        }
    }

    private UserSupport getUserSupport(
            UUID userId,
            UUID userSupportId
    ) {
        return userSupportRepository
                .findByUser_UserIdAndUserSupportId(
                        userId,
                        userSupportId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Персональная мера с id "
                                        + userSupportId
                                        + " у пользователя с id "
                                        + userId
                                        + " не найдена"
                        )
                );
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Пользователь с id "
                                        + userId
                                        + " не найден"
                        )
                );
    }

    private void ensureUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "Пользователь с id " + userId + " не найден"
            );
        }
    }
}
