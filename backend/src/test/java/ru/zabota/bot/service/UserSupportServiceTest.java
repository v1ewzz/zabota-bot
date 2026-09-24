package ru.zabota.bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.support.SupportMeasureShortResponse;
import ru.zabota.bot.dto.support.SupportSearchResponse;
import ru.zabota.bot.dto.usersupport.UserSupportResponse;
import ru.zabota.bot.dto.usersupport.UserSupportUpdateRequest;
import ru.zabota.bot.entity.DictionaryType;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Region;
import ru.zabota.bot.entity.Municipality;
import ru.zabota.bot.entity.SupportMeasure;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserSupport;
import ru.zabota.bot.exception.BadRequestException;
import ru.zabota.bot.mapper.UserSupportMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.SupportMeasureRepository;
import ru.zabota.bot.repository.UserChildRepository;
import ru.zabota.bot.repository.UserRepository;
import ru.zabota.bot.repository.UserSupportRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit-тест UserSupportService.
 *
 * Проверяет сквозной слой сохранения результата подбора,
 * повторный подбор без сброса статуса и изменение статуса меры.
 */
@ExtendWith(MockitoExtension.class)
class UserSupportServiceTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID SUPPORT_ID = UUID.randomUUID();
    private static final UUID USER_SUPPORT_ID = UUID.randomUUID();

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserChildRepository userChildRepository;

    @Mock
    private UserSupportRepository userSupportRepository;

    @Mock
    private SupportMeasureRepository supportMeasureRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private SupportMatchingService supportMatchingService;

    @Mock
    private UserSupportMapper userSupportMapper;

    private UserSupportService service;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(
                Instant.parse("2026-09-23T10:00:00Z"),
                ZoneOffset.UTC
        );

        service = new UserSupportService(
                userRepository,
                userChildRepository,
                userSupportRepository,
                supportMeasureRepository,
                dictionaryValueRepository,
                supportMatchingService,
                userSupportMapper,
                clock
        );
    }

    @Test
    void rematch_shouldCreatePersonalResult() {
        User user = createUser();
        SupportMeasure support = createSupport();
        SupportMeasureShortResponse matched = new SupportMeasureShortResponse();
        matched.setSupportId(SUPPORT_ID);
        matched.setAmount(new BigDecimal("5000.00"));

        SupportSearchResponse searchResponse = new SupportSearchResponse();
        searchResponse.setMeasures(List.of(matched));

        DictionaryValue defaultStatus =
                dictionaryValue("NOT_APPLIED", "Не оформлено");

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));
        when(userChildRepository.findAllByUser_UserId(USER_ID))
                .thenReturn(List.of());
        when(supportMatchingService.search(any()))
                .thenReturn(searchResponse);
        when(userSupportRepository.findAllByUser_UserIdOrderByCheckedAtDesc(USER_ID))
                .thenReturn(List.of());
        when(dictionaryValueRepository.findByDictionaryType_CodeAndCode(
                "USER_SUPPORT_STATUS",
                "NOT_APPLIED"
        )).thenReturn(Optional.of(defaultStatus));
        when(supportMeasureRepository.findById(SUPPORT_ID))
                .thenReturn(Optional.of(support));

        SupportSearchResponse result = service.rematch(USER_ID);

        assertEquals(USER_ID, result.getUserId());
        assertEquals(1, result.getMeasures().size());
        verify(userSupportRepository).save(any(UserSupport.class));
    }

    @Test
    void update_shouldRejectInvalidStatusType() {
        UserSupport userSupport = new UserSupport();
        userSupport.setUserSupportId(USER_SUPPORT_ID);

        DictionaryValue wrongStatus = dictionaryValue(
                "GOSUSLUGI",
                "Госуслуги"
        );
        DictionaryType wrongType = new DictionaryType();
        wrongType.setCode("APPLICATION_CHANNEL");
        wrongStatus.setDictionaryType(wrongType);

        UserSupportUpdateRequest request =
                new UserSupportUpdateRequest();
        request.setStatusId(UUID.randomUUID());

        when(userSupportRepository.findByUser_UserIdAndUserSupportId(
                USER_ID,
                USER_SUPPORT_ID
        )).thenReturn(Optional.of(userSupport));
        when(dictionaryValueRepository.findById(request.getStatusId()))
                .thenReturn(Optional.of(wrongStatus));

        assertThrows(
                BadRequestException.class,
                () -> service.update(
                        USER_ID,
                        USER_SUPPORT_ID,
                        request
                )
        );
    }

    @Test
    void getAll_shouldReturnMappedResults() {
        UserSupport userSupport = new UserSupport();
        UserSupportResponse response = new UserSupportResponse();
        response.setUserSupportId(USER_SUPPORT_ID);

        when(userRepository.existsById(USER_ID))
                .thenReturn(true);
        when(userSupportRepository.findAllByUser_UserIdOrderByCheckedAtDesc(USER_ID))
                .thenReturn(List.of(userSupport));
        when(userSupportMapper.toResponse(userSupport))
                .thenReturn(response);

        List<UserSupportResponse> result = service.getAll(USER_ID);

        assertEquals(1, result.size());
        assertEquals(USER_SUPPORT_ID, result.get(0).getUserSupportId());
    }

    private User createUser() {
        User user = new User();
        user.setUserId(USER_ID);

        Region region = new Region();
        region.setRegionId(UUID.randomUUID());
        user.setRegion(region);

        Municipality municipality = new Municipality();
        municipality.setMunicipalityId(UUID.randomUUID());
        municipality.setRegion(region);
        user.setMunicipality(municipality);

        user.setFamilyRelation(dictionaryValue("WIFE", "Жена"));
        user.setMilitaryStatus(dictionaryValue("MOBILIZED", "Мобилизованный"));

        return user;
    }

    private SupportMeasure createSupport() {
        SupportMeasure support = new SupportMeasure();
        support.setSupportId(SUPPORT_ID);
        support.setName("Тестовая мера");
        support.setDescription("Описание");
        support.setAmount(new BigDecimal("3000.00"));
        return support;
    }

    private DictionaryValue dictionaryValue(String code, String label) {
        DictionaryValue value = new DictionaryValue();
        value.setDictionaryValueId(UUID.randomUUID());
        value.setCode(code);
        value.setLabel(label);
        return value;
    }
}
