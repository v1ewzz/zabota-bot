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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * Сервис для работы с пользователями и их профилями.
 *
 * Отвечает за:
 *
 * - создание пользователя;
 * - получение пользователя;
 * - получение полного профиля вместе с детьми;
 * - обновление пользователя;
 * - удаление пользователя;
 * - разрешение связанных Region, Municipality и DictionaryValue;
 * - создание и синхронизацию UserChild.
 *
 * При обновлении список детей полностью синхронизируется
 * с переданным профилем.
 *
 * Сервис не содержит HTTP-логики и не реализует алгоритм
 * подбора мер социальной поддержки.
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
        Region region = getRegion(request.getRegionId());

        Municipality municipality =
                getMunicipality(request.getMunicipalityId());

        validateMunicipalityRegion(
                municipality,
                region
        );

        DictionaryValue familyRelation =
                getOptionalDictionaryValue(
                        request.getFamilyRelationId(),
                        "FAMILY_RELATION",
                        "Отношение к военнослужащему"
                );

        DictionaryValue militaryStatus =
                getOptionalDictionaryValue(
                        request.getMilitaryStatusId(),
                        "MILITARY_STATUS",
                        "Статус военнослужащего"
                );

        DictionaryValue disabilityGroup =
                getOptionalDictionaryValue(
                        request.getDisabilityGroupId(),
                        "DISABILITY_GROUP",
                        "Группа инвалидности"
                );

        DictionaryValue employmentStatus =
                getOptionalDictionaryValue(
                        request.getEmploymentStatusId(),
                        "EMPLOYMENT_STATUS",
                        "Статус занятости"
                );

        DictionaryValue incomeCategory =
                getOptionalDictionaryValue(
                        request.getIncomeCategoryId(),
                        "INCOME_CATEGORY",
                        "Категория дохода"
                );

        User user = userMapper.toEntity(
                request,
                familyRelation,
                militaryStatus,
                disabilityGroup,
                employmentStatus,
                incomeCategory
        );

        user.setRegion(region);
        user.setMunicipality(municipality);

        user = userRepository.save(user);

        saveChildren(
                user,
                request.getChildren()
        );

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        User user = getUser(id);

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID id) {
        User user = getUser(id);

        List<UserChildResponse> children =
                userChildRepository
                        .findAllByUser_UserId(id)
                        .stream()
                        .map(userChildMapper::toResponse)
                        .toList();

        List<UserSupportResponse> supports =
                userSupportService == null
                        ? List.of()
                        : userSupportService.getAll(id);

        UserProfileResponse response = userMapper.toProfileResponse(
                user,
                children
        );
        response.setSupports(supports);

        return response;
    }

    @Transactional
    public UserResponse update(
            UUID id,
            UserRequest request
    ) {
        User user = getUser(id);

        Region region = getRegion(request.getRegionId());

        Municipality municipality =
                getMunicipality(request.getMunicipalityId());

        validateMunicipalityRegion(
                municipality,
                region
        );

        DictionaryValue familyRelation =
                getOptionalDictionaryValue(
                        request.getFamilyRelationId(),
                        "FAMILY_RELATION",
                        "Отношение к военнослужащему"
                );

        DictionaryValue militaryStatus =
                getOptionalDictionaryValue(
                        request.getMilitaryStatusId(),
                        "MILITARY_STATUS",
                        "Статус военнослужащего"
                );

        DictionaryValue disabilityGroup =
                getOptionalDictionaryValue(
                        request.getDisabilityGroupId(),
                        "DISABILITY_GROUP",
                        "Группа инвалидности"
                );

        DictionaryValue employmentStatus =
                getOptionalDictionaryValue(
                        request.getEmploymentStatusId(),
                        "EMPLOYMENT_STATUS",
                        "Статус занятости"
                );

        DictionaryValue incomeCategory =
                getOptionalDictionaryValue(
                        request.getIncomeCategoryId(),
                        "INCOME_CATEGORY",
                        "Категория дохода"
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

        user = userRepository.save(user);

        userChildRepository.deleteAllByUser_UserId(id);

        saveChildren(
                user,
                request.getChildren()
        );

        return userMapper.toResponse(user);
    }

    @Transactional
    public void delete(UUID id) {
        User user = getUser(id);

        userRepository.delete(user);
    }

    private User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Пользователь с id "
                                        + id
                                        + " не найден"
                        )
                );
    }

    private Region getRegion(UUID id) {
        return regionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Регион с id "
                                        + id
                                        + " не найден"
                        )
                );
    }

    private Municipality getMunicipality(UUID id) {
        return municipalityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Муниципалитет с id "
                                        + id
                                        + " не найден"
                        )
                );
    }

    private void validateMunicipalityRegion(
            Municipality municipality,
            Region region
    ) {
        if (municipality.getRegion() != null
                && municipality.getRegion().getRegionId() != null
                && !region.getRegionId().equals(
                municipality.getRegion().getRegionId()
        )) {
            throw new BadRequestException(
                    "Муниципалитет не относится к выбранному региону"
            );
        }
    }

    private DictionaryValue getOptionalDictionaryValue(
            UUID id,
            String expectedType,
            String fieldName
    ) {
        if (id == null) {
            return null;
        }

        DictionaryValue value = dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                fieldName
                                        + " с id "
                                        + id
                                        + " не найден"
                        )
                );

        validateDictionaryValue(
                value,
                expectedType,
                fieldName
        );

        return value;
    }

    private void validateDictionaryValue(
            DictionaryValue value,
            String expectedType,
            String fieldName
    ) {
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
    }

    private void saveChildren(
            User user,
            List<UserChildRequest> requests
    ) {
        if (requests == null || requests.isEmpty()) {
            return;
        }

        List<UserChild> children =
                new ArrayList<>();

        for (UserChildRequest request : requests) {
            DictionaryValue educationLevel =
                    getRequiredDictionaryValue(
                            request.getEducationLevelId(),
                            "EDUCATION_LEVEL",
                            "Уровень образования"
                    );

            DictionaryValue disabilityGroup =
                    getOptionalDictionaryValue(
                            request.getDisabilityGroupId(),
                            "DISABILITY_GROUP",
                            "Группа инвалидности ребёнка"
                    );

            UserChild child =
                    userChildMapper.toEntity(
                            request,
                            user,
                            educationLevel,
                            disabilityGroup
                    );

            children.add(child);
        }

        userChildRepository.saveAll(children);
    }

    private DictionaryValue getRequiredDictionaryValue(
            UUID id,
            String expectedType,
            String fieldName
    ) {
        if (id == null) {
            throw new BadRequestException(
                    fieldName + " обязателен"
            );
        }

        DictionaryValue value = dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                fieldName
                                        + " с id "
                                        + id
                                        + " не найден"
                        )
                );

        validateDictionaryValue(
                value,
                expectedType,
                fieldName
        );

        return value;
    }
}