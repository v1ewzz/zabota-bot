package ru.zabota.bot.mapper;

import org.junit.jupiter.api.Test;
import ru.zabota.bot.dto.user.UserChildRequest;
import ru.zabota.bot.dto.user.UserChildResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.User;
import ru.zabota.bot.entity.UserChild;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * Unit-тест UserChildMapper.
 *
 * Проверяет преобразование Request → Entity,
 * Entity → Response, обновление существующей Entity
 * и обработку null.
 */
class UserChildMapperTest {

    private final UserChildMapper mapper = new UserChildMapper();

    @Test
    void shouldMapRequestToEntity() {
        UUID userId = UUID.randomUUID();
        UUID educationLevelId = UUID.randomUUID();
        UUID disabilityGroupId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);

        DictionaryValue educationLevel = createDictionaryValue(
                educationLevelId,
                "SCHOOL",
                "Школьное образование"
        );

        DictionaryValue disabilityGroup = createDictionaryValue(
                disabilityGroupId,
                "GROUP_2",
                "II группа"
        );

        UserChildRequest request = new UserChildRequest();

        request.setBirthDate(LocalDate.of(2012, 5, 10));
        request.setEducationLevelId(educationLevelId);
        request.setGrade((short) 7);
        request.setDisability(true);
        request.setDisabilityGroupId(disabilityGroupId);
        request.setFullTime(true);

        UserChild entity = mapper.toEntity(
                request,
                user,
                educationLevel,
                disabilityGroup
        );

        assertEquals(user, entity.getUser());
        assertEquals(
                LocalDate.of(2012, 5, 10),
                entity.getBirthDate()
        );
        assertEquals(
                educationLevel,
                entity.getEducationLevel()
        );
        assertEquals((short) 7, entity.getGrade());
        assertEquals(true, entity.isDisability());
        assertEquals(disabilityGroup, entity.getDisabilityGroup());
        assertEquals(true, entity.isFullTime());
    }

    @Test
    void shouldMapEntityToResponse() {
        UUID childId = UUID.randomUUID();
        UUID educationLevelId = UUID.randomUUID();
        UUID disabilityGroupId = UUID.randomUUID();

        DictionaryValue educationLevel = createDictionaryValue(
                educationLevelId,
                "SCHOOL",
                "Школьное образование"
        );

        DictionaryValue disabilityGroup = createDictionaryValue(
                disabilityGroupId,
                "GROUP_2",
                "II группа"
        );

        UserChild entity = new UserChild();

        entity.setChildId(childId);
        entity.setBirthDate(LocalDate.of(2012, 5, 10));
        entity.setEducationLevel(educationLevel);
        entity.setGrade((short) 7);
        entity.setDisability(true);
        entity.setDisabilityGroup(disabilityGroup);
        entity.setFullTime(true);

        UserChildResponse response = mapper.toResponse(entity);

        assertEquals(childId, response.getChildId());
        assertEquals(
                LocalDate.of(2012, 5, 10),
                response.getBirthDate()
        );

        assertEquals(
                educationLevelId,
                response.getEducationLevelId()
        );
        assertEquals(
                "SCHOOL",
                response.getEducationLevelCode()
        );
        assertEquals(
                "Школьное образование",
                response.getEducationLevelName()
        );

        assertEquals((short) 7, response.getGrade());

        assertEquals(true, response.isDisability());

        assertEquals(
                disabilityGroupId,
                response.getDisabilityGroupId()
        );
        assertEquals(
                "GROUP_2",
                response.getDisabilityGroupCode()
        );
        assertEquals(
                "II группа",
                response.getDisabilityGroupName()
        );

        assertEquals(true, response.isFullTime());
    }

    @Test
    void shouldMapChildWithoutOptionalDisabilityGroup() {
        UserChild entity = new UserChild();

        DictionaryValue educationLevel = createDictionaryValue(
                UUID.randomUUID(),
                "SCHOOL",
                "Школьное образование"
        );

        entity.setBirthDate(LocalDate.of(2012, 5, 10));
        entity.setEducationLevel(educationLevel);
        entity.setDisability(false);
        entity.setFullTime(true);

        UserChildResponse response = mapper.toResponse(entity);

        assertEquals(
                "SCHOOL",
                response.getEducationLevelCode()
        );
        assertEquals(
                "Школьное образование",
                response.getEducationLevelName()
        );

        assertEquals(false, response.isDisability());
        assertNull(response.getDisabilityGroupId());
        assertNull(response.getDisabilityGroupCode());
        assertNull(response.getDisabilityGroupName());
    }

    @Test
    void shouldUpdateExistingEntity() {
        UserChild entity = new UserChild();

        UserChildRequest request = new UserChildRequest();

        request.setBirthDate(LocalDate.of(2015, 3, 20));
        request.setGrade((short) 4);
        request.setDisability(false);
        request.setFullTime(true);

        User user = new User();

        DictionaryValue educationLevel = createDictionaryValue(
                UUID.randomUUID(),
                "SCHOOL",
                "Школьное образование"
        );

        mapper.updateEntity(
                entity,
                request,
                user,
                educationLevel,
                null
        );

        assertEquals(user, entity.getUser());
        assertEquals(
                LocalDate.of(2015, 3, 20),
                entity.getBirthDate()
        );
        assertEquals(educationLevel, entity.getEducationLevel());
        assertEquals((short) 4, entity.getGrade());
        assertEquals(false, entity.isDisability());
        assertNull(entity.getDisabilityGroup());
        assertEquals(true, entity.isFullTime());
    }

    @Test
    void shouldReturnNullWhenRequestIsNull() {
        UserChild entity = mapper.toEntity(
                null,
                null,
                null,
                null
        );

        assertNull(entity);
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        UserChildResponse response = mapper.toResponse(null);

        assertNull(response);
    }

    private DictionaryValue createDictionaryValue(
            UUID id,
            String code,
            String label
    ) {
        DictionaryValue value = new DictionaryValue();

        value.setDictionaryValueId(id);
        value.setCode(code);
        value.setLabel(label);
        value.setActive(true);

        return value;
    }
}