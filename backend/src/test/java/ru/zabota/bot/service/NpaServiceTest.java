package ru.zabota.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.npa.NpaRequest;
import ru.zabota.bot.dto.npa.NpaResponse;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.entity.Npa;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.NpaMapper;
import ru.zabota.bot.repository.DictionaryValueRepository;
import ru.zabota.bot.repository.NpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit-тест NpaService.
 *
 * Проверяет CRUD-операции и обработку отсутствующих
 * связанных DictionaryValue.
 *
 * Spring-контекст и база данных не используются.
 * Все внешние зависимости заменены Mockito-моками.
 */
@ExtendWith(MockitoExtension.class)
class NpaServiceTest {

    @Mock
    private NpaRepository npaRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private NpaMapper npaMapper;

    @InjectMocks
    private NpaService npaService;

    @Test
    void shouldCreateNpa() {
        UUID typeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        UUID npaId = UUID.randomUUID();

        NpaRequest request = createRequest(
                typeId,
                levelId,
                statusId
        );

        DictionaryValue type = createDictionaryValue(
                typeId,
                "LAW",
                "Закон"
        );

        DictionaryValue level = createDictionaryValue(
                levelId,
                "FEDERAL",
                "Федеральный"
        );

        DictionaryValue status = createDictionaryValue(
                statusId,
                "ACTIVE",
                "Действующий"
        );

        Npa npa = new Npa();
        NpaResponse expected = createResponse(npaId);

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.of(type));

        when(dictionaryValueRepository.findById(levelId))
                .thenReturn(Optional.of(level));

        when(dictionaryValueRepository.findById(statusId))
                .thenReturn(Optional.of(status));

        when(npaMapper.toEntity(
                request,
                type,
                level,
                status
        )).thenReturn(npa);

        when(npaRepository.save(npa))
                .thenReturn(npa);

        when(npaMapper.toResponse(npa))
                .thenReturn(expected);

        NpaResponse result = npaService.create(request);

        assertEquals(expected, result);

        verify(dictionaryValueRepository).findById(typeId);
        verify(dictionaryValueRepository).findById(levelId);
        verify(dictionaryValueRepository).findById(statusId);

        verify(npaMapper).toEntity(
                request,
                type,
                level,
                status
        );

        verify(npaRepository).save(npa);
        verify(npaMapper).toResponse(npa);
    }

    @Test
    void shouldThrowExceptionWhenNpaTypeNotFoundOnCreate() {
        UUID typeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        NpaRequest request = createRequest(
                typeId,
                levelId,
                statusId
        );

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> npaService.create(request)
        );

        verify(dictionaryValueRepository).findById(typeId);

        verify(dictionaryValueRepository, never())
                .findById(levelId);

        verify(dictionaryValueRepository, never())
                .findById(statusId);

        verify(npaRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowExceptionWhenLevelNotFoundOnCreate() {
        UUID typeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        NpaRequest request = createRequest(
                typeId,
                levelId,
                statusId
        );

        DictionaryValue type = createDictionaryValue(
                typeId,
                "LAW",
                "Закон"
        );

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.of(type));

        when(dictionaryValueRepository.findById(levelId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> npaService.create(request)
        );

        verify(dictionaryValueRepository).findById(typeId);
        verify(dictionaryValueRepository).findById(levelId);

        verify(dictionaryValueRepository, never())
                .findById(statusId);

        verify(npaRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowExceptionWhenStatusNotFoundOnCreate() {
        UUID typeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        NpaRequest request = createRequest(
                typeId,
                levelId,
                statusId
        );

        DictionaryValue type = createDictionaryValue(
                typeId,
                "LAW",
                "Закон"
        );

        DictionaryValue level = createDictionaryValue(
                levelId,
                "FEDERAL",
                "Федеральный"
        );

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.of(type));

        when(dictionaryValueRepository.findById(levelId))
                .thenReturn(Optional.of(level));

        when(dictionaryValueRepository.findById(statusId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> npaService.create(request)
        );

        verify(dictionaryValueRepository).findById(typeId);
        verify(dictionaryValueRepository).findById(levelId);
        verify(dictionaryValueRepository).findById(statusId);

        verify(npaRepository, never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldGetNpaById() {
        UUID id = UUID.randomUUID();

        Npa npa = new Npa();
        NpaResponse expected = createResponse(id);

        when(npaRepository.findById(id))
                .thenReturn(Optional.of(npa));

        when(npaMapper.toResponse(npa))
                .thenReturn(expected);

        NpaResponse result = npaService.getById(id);

        assertEquals(expected, result);

        verify(npaRepository).findById(id);
        verify(npaMapper).toResponse(npa);
    }

    @Test
    void shouldThrowExceptionWhenNpaNotFound() {
        UUID id = UUID.randomUUID();

        when(npaRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> npaService.getById(id)
        );

        verify(npaRepository).findById(id);

        verify(npaMapper, never())
                .toResponse(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldGetAllNpa() {
        Npa first = new Npa();
        Npa second = new Npa();

        NpaResponse firstResponse =
                createResponse(UUID.randomUUID());

        NpaResponse secondResponse =
                createResponse(UUID.randomUUID());

        when(npaRepository.findAll())
                .thenReturn(List.of(first, second));

        when(npaMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(npaMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<NpaResponse> result = npaService.getAll();

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(npaRepository).findAll();
        verify(npaMapper).toResponse(first);
        verify(npaMapper).toResponse(second);
    }

    @Test
    void shouldUpdateNpa() {
        UUID npaId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        NpaRequest request = createRequest(
                typeId,
                levelId,
                statusId
        );

        Npa npa = new Npa();

        DictionaryValue type = createDictionaryValue(
                typeId,
                "LAW",
                "Закон"
        );

        DictionaryValue level = createDictionaryValue(
                levelId,
                "FEDERAL",
                "Федеральный"
        );

        DictionaryValue status = createDictionaryValue(
                statusId,
                "ACTIVE",
                "Действующий"
        );

        NpaResponse expected = createResponse(npaId);

        when(npaRepository.findById(npaId))
                .thenReturn(Optional.of(npa));

        when(dictionaryValueRepository.findById(typeId))
                .thenReturn(Optional.of(type));

        when(dictionaryValueRepository.findById(levelId))
                .thenReturn(Optional.of(level));

        when(dictionaryValueRepository.findById(statusId))
                .thenReturn(Optional.of(status));

        when(npaRepository.save(npa))
                .thenReturn(npa);

        when(npaMapper.toResponse(npa))
                .thenReturn(expected);

        NpaResponse result =
                npaService.update(npaId, request);

        assertEquals(expected, result);

        verify(npaRepository).findById(npaId);

        verify(dictionaryValueRepository).findById(typeId);
        verify(dictionaryValueRepository).findById(levelId);
        verify(dictionaryValueRepository).findById(statusId);

        verify(npaMapper).updateEntity(
                npa,
                request,
                type,
                level,
                status
        );

        verify(npaRepository).save(npa);
        verify(npaMapper).toResponse(npa);
    }

    @Test
    void shouldDeleteNpa() {
        UUID id = UUID.randomUUID();

        Npa npa = new Npa();

        when(npaRepository.findById(id))
                .thenReturn(Optional.of(npa));

        npaService.delete(id);

        verify(npaRepository).findById(id);
        verify(npaRepository).delete(npa);
    }

    @Test
    void shouldThrowExceptionWhenNpaNotFoundOnDelete() {
        UUID id = UUID.randomUUID();

        when(npaRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> npaService.delete(id)
        );

        verify(npaRepository).findById(id);

        verify(npaRepository, never())
                .delete(org.mockito.ArgumentMatchers.any());
    }

    private NpaRequest createRequest(
            UUID typeId,
            UUID levelId,
            UUID statusId
    ) {
        NpaRequest request = new NpaRequest();

        request.setName("Закон Республики Татарстан");
        request.setNpaTypeId(typeId);
        request.setNumber("100-ЗРТ");
        request.setAdoptionDate(LocalDate.of(2024, 12, 25));
        request.setValidFrom(LocalDate.of(2025, 1, 1));
        request.setValidTo(null);
        request.setLevelId(levelId);
        request.setStatusId(statusId);
        request.setOfficialUrl(
                "https://pravo.tatarstan.ru/"
        );

        return request;
    }

    private DictionaryValue createDictionaryValue(
            UUID id,
            String code,
            String label
    ) {
        DictionaryValue value =
                new DictionaryValue();

        value.setDictionaryValueId(id);
        value.setCode(code);
        value.setLabel(label);

        return value;
    }

    private NpaResponse createResponse(UUID id) {
        NpaResponse response =
                new NpaResponse();

        response.setNpaId(id);
        response.setName(
                "Закон Республики Татарстан"
        );
        response.setNumber("100-ЗРТ");

        return response;
    }
}