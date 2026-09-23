package ru.zabota.bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.dto.user.UserProfileResponse;
import ru.zabota.bot.dto.user.UserRequest;
import ru.zabota.bot.dto.user.UserResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserChild;
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

    public UserService(
            UserRepository userRepository,
            UserChildRepository userChildRepository,
            RegionRepository regionRepository,
            MunicipalityRepository municipalityRepository,
            DictionaryValueRepository dictionaryValueRepository,
            UserMapper userMapper,
            UserChildMapper userChildMapper
    ) {
        this.userRepository = userRepository;
        this.userChildRepository = userChildRepository;
        this.regionRepository = regionRepository;
        this.municipalityRepository = municipalityRepository;
        this.dictionaryValueRepository = dictionaryValueRepository;
        this.userMapper = userMapper;
        this.userChildMapper = userChildMapper;
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        Region region = getRegion(request.getRegionId());

        Municipality municipality =
                getMunicipality(request.getMunicipalityId());

        DictionaryValue familyRelation =
                getOptionalDictionaryValue(
                        request.getFamilyRelationId()
                );

        DictionaryValue militaryStatus =
                getOptionalDictionaryValue(
                        request.getMilitaryStatusId()
                );

        DictionaryValue disabilityGroup =
                getOptionalDictionaryValue(
                        request.getDisabilityGroupId()
                );

        DictionaryValue employmentStatus =
                getOptionalDictionaryValue(
                        request.getEmploymentStatusId()
                );

        DictionaryValue incomeCategory =
                getOptionalDictionaryValue(
                        request.getIncomeCategoryId()
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

        return userMapper.toProfileResponse(
                user,
                children
        );
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

        DictionaryValue familyRelation =
                getOptionalDictionaryValue(
                        request.getFamilyRelationId()
                );

        DictionaryValue militaryStatus =
                getOptionalDictionaryValue(
                        request.getMilitaryStatusId()
                );

        DictionaryValue disabilityGroup =
                getOptionalDictionaryValue(
                        request.getDisabilityGroupId()
                );

        DictionaryValue employmentStatus =
                getOptionalDictionaryValue(
                        request.getEmploymentStatusId()
                );

        DictionaryValue incomeCategory =
                getOptionalDictionaryValue(
                        request.getIncomeCategoryId()
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

    private DictionaryValue getOptionalDictionaryValue(UUID id) {
        if (id == null) {
            return null;
        }

        return dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Значение справочника с id "
                                        + id
                                        + " не найдено"
                        )
                );
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
                            "Уровень образования"
                    );

            DictionaryValue disabilityGroup =
                    getOptionalDictionaryValue(
                            request.getDisabilityGroupId()
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
            String fieldName
    ) {
        return dictionaryValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                fieldName
                                        + " с id "
                                        + id
                                        + " не найден"
                        )
                );
    }
}