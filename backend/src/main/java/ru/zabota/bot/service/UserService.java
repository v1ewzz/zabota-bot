package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserChild;
import ru.zabota.bot.exception.BadRequestException;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.UserChildMapper;
import ru.zabota.bot.mapper.UserMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.MunicipalityRepository;
import ru.zabota.bot.repository.RegionRepository;
import ru.zabota.bot.repository.UserChildRepository;
import ru.zabota.bot.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Сервис профиля пользователя.
 *
 * Сохраняет все параметры анкеты, детей и предоставляет
 * отдельную операцию обновления с немедленным повторным подбором.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserChildRepository userChildRepository;
    private final RegionRepository regionRepository;
    private final MunicipalityRepository municipalityRepository;
    private final DictionaryValueRepository dictionaryValueRepository;
    private final UserMapper userMapper;
    private final UserChildMapper userChildMapper;
    private final UserSupportService userSupportService;

    public UserService(
            UserRepository userRepository,
            UserChildRepository userChildRepository,
            RegionRepository regionRepository,
            MunicipalityRepository municipalityRepository,
            DictionaryValueRepository dictionaryValueRepository,
            UserMapper userMapper,
            UserChildMapper userChildMapper,
            UserSupportService userSupportService
    ) {
        this.userRepository = userRepository;
        this.userChildRepository = userChildRepository;
        this.regionRepository = regionRepository;
        this.municipalityRepository = municipalityRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.userMapper = userMapper;
        this.userChildMapper = userChildMapper;
        this.userSupportService = userSupportService;
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        validateRequestDates(request);

        Region region = getRegion(request.getRegionId());
        Municipality municipality = getMunicipality(request.getMunicipalityId());
        validateMunicipalityRegion(municipality, region);

        DictionaryValue familyRelation = getOptionalDictionaryValue(
                request.getFamilyRelationId(), "FAMILY_RELATION", "Отношение к военнослужащему"
        );
        DictionaryValue militaryStatus = getOptionalDictionaryValue(
                request.getMilitaryStatusId(), "MILITARY_STATUS", "Статус военнослужащего"
        );
        DictionaryValue sex = getOptionalDictionaryValue(
                request.getSexId(), "SEX", "Пол"
        );
        DictionaryValue disabilityGroup = getOptionalDictionaryValue(
                request.getDisabilityGroupId(), "DISABILITY_GROUP", "Группа инвалидности"
        );
        DictionaryValue employmentStatus = getOptionalDictionaryValue(
                request.getEmploymentStatusId(), "EMPLOYMENT_STATUS", "Статус занятости"
        );
        DictionaryValue incomeCategory = getOptionalDictionaryValue(
                request.getIncomeCategoryId(), "INCOME_CATEGORY", "Категория дохода"
        );
        DictionaryValue legalIssueCategory = getOptionalDictionaryValue(
                request.getLegalIssueCategoryId(), "LEGAL_ISSUE_CATEGORY", "Категория юридического вопроса"
        );

        User user = userMapper.toEntity(
                request,
                familyRelation,
                militaryStatus,
                disabilityGroup,
                employmentStatus,
                incomeCategory
        );
        user.setSex(sex);
        user.setLegalIssueCategory(legalIssueCategory);
        user.setRegion(region);
        user.setMunicipality(municipality);
        user = userRepository.save(user);

        saveChildren(user, request.getChildren());
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        return userMapper.toResponse(getUser(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID id) {
        User user = getUser(id);

        List<UserChildResponse> children = userChildRepository
                .findAllByUser_UserId(id)
                .stream()
                .map(userChildMapper::toResponse)
                .toList();

        List<UserSupportResponse> supports = userSupportService.getAll(id);
        UserProfileResponse response = userMapper.toProfileResponse(user, children);
        response.setSupports(supports);
        return response;
    }

    @Transactional
    public UserResponse update(UUID id, UserRequest request) {
        User user = getUser(id);
        applyUpdate(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserProfileResponse updateAndRematch(UUID id, UserRequest request) {
        update(id, request);
        userSupportService.rematch(id);
        return getProfile(id);
    }

    @Transactional
    public void delete(UUID id) {
        userRepository.delete(getUser(id));
    }

    private void applyUpdate(User user, UserRequest request) {
        validateRequestDates(request);

        Region region = getRegion(request.getRegionId());
        Municipality municipality = getMunicipality(request.getMunicipalityId());
        validateMunicipalityRegion(municipality, region);

        DictionaryValue familyRelation = getOptionalDictionaryValue(
                request.getFamilyRelationId(), "FAMILY_RELATION", "Отношение к военнослужащему"
        );
        DictionaryValue militaryStatus = getOptionalDictionaryValue(
                request.getMilitaryStatusId(), "MILITARY_STATUS", "Статус военнослужащего"
        );
        DictionaryValue sex = getOptionalDictionaryValue(
                request.getSexId(), "SEX", "Пол"
        );
        DictionaryValue disabilityGroup = getOptionalDictionaryValue(
                request.getDisabilityGroupId(), "DISABILITY_GROUP", "Группа инвалидности"
        );
        DictionaryValue employmentStatus = getOptionalDictionaryValue(
                request.getEmploymentStatusId(), "EMPLOYMENT_STATUS", "Статус занятости"
        );
        DictionaryValue incomeCategory = getOptionalDictionaryValue(
                request.getIncomeCategoryId(), "INCOME_CATEGORY", "Категория дохода"
        );
        DictionaryValue legalIssueCategory = getOptionalDictionaryValue(
                request.getLegalIssueCategoryId(), "LEGAL_ISSUE_CATEGORY", "Категория юридического вопроса"
        );

        userMapper.updateEntity(
                user,
                request,
                region,
                municipality,
                familyRelation,
                militaryStatus,
                disabilityGroup,
                employmentStatus,
                incomeCategory
        );
        user.setSex(sex);
        user.setLegalIssueCategory(legalIssueCategory);

        userChildRepository.deleteAllByUser_UserId(user.getUserId());
        saveChildren(user, request.getChildren());
    }

    private void validateRequestDates(UserRequest request) {
        if (request == null) {
            throw new BadRequestException("Профиль пользователя не может быть пустым");
        }

        LocalDate today = LocalDate.now();

        if (request.getBirthDate() != null && request.getBirthDate().isAfter(today)) {
            throw new BadRequestException("Дата рождения не может быть в будущем");
        }

        if (request.getPregnancyDays() != null && request.getPregnancyDays() < 0) {
            throw new BadRequestException("Срок беременности не может быть отрицательным");
        }

        if (request.getPregnancyDays() != null && request.getPregnancyDays() > 294) {
            throw new BadRequestException("Срок беременности указан некорректно");
        }

        if (request.getServicemanLeaveStart() != null
                && request.getServicemanLeaveEnd() != null
                && request.getServicemanLeaveStart().isAfter(request.getServicemanLeaveEnd())) {
            throw new BadRequestException("Дата окончания отпуска не может быть раньше даты начала");
        }

        if (request.getChildren() != null) {
            for (UserChildRequest child : request.getChildren()) {
                if (child.getBirthDate() != null && child.getBirthDate().isAfter(today)) {
                    throw new BadRequestException("Дата рождения ребёнка не может быть в будущем");
                }
                if (child.getGrade() != null && (child.getGrade() < 0 || child.getGrade() > 13)) {
                    throw new BadRequestException("Класс ребёнка указан некорректно");
                }
            }
        }
    }

    private void saveChildren(User user, List<UserChildRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return;
        }

        List<UserChild> children = new ArrayList<>();

        for (UserChildRequest request : requests) {
            DictionaryValue educationLevel = getRequiredDictionaryValue(
                    request.getEducationLevelId(), "EDUCATION_LEVEL", "Уровень образования"
            );
            DictionaryValue disabilityGroup = getOptionalDictionaryValue(
                    request.getDisabilityGroupId(), "DISABILITY_GROUP", "Группа инвалидности ребёнка"
            );
            DictionaryValue institutionType = getOptionalDictionaryValue(
                    request.getInstitutionTypeId(), "EDUCATION_ORGANIZATION_TYPE", "Тип образовательной организации ребёнка"
            );

            UserChild child = userChildMapper.toEntity(
                    request,
                    user,
                    educationLevel,
                    disabilityGroup
            );
            child.setInstitutionType(institutionType);
            children.add(child);
        }

        userChildRepository.saveAll(children);
    }

    private User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    private Region getRegion(UUID id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Регион с id " + id + " не найден"));
    }

    private Municipality getMunicipality(UUID id) {
        return municipalityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Муниципалитет с id " + id + " не найден"));
    }

    private void validateMunicipalityRegion(Municipality municipality, Region region) {
        if (municipality.getRegion() != null
                && municipality.getRegion().getRegionId() != null
                && !region.getRegionId().equals(municipality.getRegion().getRegionId())) {
            throw new BadRequestException("Муниципалитет не относится к выбранному региону");
        }
    }

    private DictionaryValue getOptionalDictionaryValue(UUID id, String expectedType, String fieldName) {
        if (id == null) {
            return null;
        }

        DictionaryValue value = dictionaryValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(fieldName + " с id " + id + " не найден"));
        validateDictionaryValue(value, expectedType, fieldName);
        return value;
    }

    private DictionaryValue getRequiredDictionaryValue(UUID id, String expectedType, String fieldName) {
        if (id == null) {
            throw new BadRequestException(fieldName + " обязателен");
        }
        return getOptionalDictionaryValue(id, expectedType, fieldName);
    }

    private void validateDictionaryValue(DictionaryValue value, String expectedType, String fieldName) {
        if (!value.isActive()) {
            throw new BadRequestException(fieldName + " недоступен для выбора");
        }
        if (value.getDictionaryType() != null
                && !expectedType.equals(value.getDictionaryType().getCode())) {
            throw new BadRequestException(fieldName + " принадлежит другому справочнику");
        }
    }
}
