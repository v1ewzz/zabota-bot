package ru.zabota.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zabota.bot.dto.dictionary.DictionaryTypeResponse;
import ru.zabota.bot.dto.dictionary.DictionaryValueResponse;
import ru.zabota.bot.entity.DictionaryType;
import ru.zabota.bot.entity.DictionaryValue;
import ru.zabota.bot.exception.ResourceNotFoundException;
import ru.zabota.bot.mapper.DictionaryTypeMapper;
import ru.zabota.bot.mapper.DictionaryValueMapper;
import ru.zabota.bot.repository.DictionaryTypeRepository;
import ru.zabota.bot.repository.DictionaryValueRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit-тест DictionaryService.
 *
 * Проверяет получение типов справочников, получение значений
 * конкретного справочника и обработку отсутствующих ресурсов.
 *
 * Spring-контекст и база данных не запускаются.
 * Repository и Mapper заменяются Mockito-моками.
 */
@ExtendWith(MockitoExtension.class)
class DictionaryServiceTest {

    @Mock
    private DictionaryTypeRepository dictionaryTypeRepository;

    @Mock
    private DictionaryValueRepository dictionaryValueRepository;

    @Mock
    private DictionaryTypeMapper dictionaryTypeMapper;

    @Mock
    private DictionaryValueMapper dictionaryValueMapper;

    @InjectMocks
    private DictionaryService dictionaryService;

    @Test
    void shouldGetDictionaryTypes() {
        DictionaryType first = new DictionaryType();
        first.setDictionaryTypeId(UUID.randomUUID());
        first.setCode("MILITARY_STATUS");
        first.setName("Военный статус");

        DictionaryType second = new DictionaryType();
        second.setDictionaryTypeId(UUID.randomUUID());
        second.setCode("FAMILY_RELATION");
        second.setName("Семейное положение");

        DictionaryTypeResponse firstResponse = new DictionaryTypeResponse();
        firstResponse.setDictionaryTypeId(first.getDictionaryTypeId());
        firstResponse.setCode(first.getCode());
        firstResponse.setName(first.getName());

        DictionaryTypeResponse secondResponse = new DictionaryTypeResponse();
        secondResponse.setDictionaryTypeId(second.getDictionaryTypeId());
        secondResponse.setCode(second.getCode());
        secondResponse.setName(second.getName());

        when(dictionaryTypeRepository.findAll())
                .thenReturn(List.of(first, second));

        when(dictionaryTypeMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(dictionaryTypeMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<DictionaryTypeResponse> result =
                dictionaryService.getDictionaryTypes();

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(dictionaryTypeRepository).findAll();
        verify(dictionaryTypeMapper).toResponse(first);
        verify(dictionaryTypeMapper).toResponse(second);
    }

    @Test
    void shouldGetDictionaryTypeById() {
        UUID id = UUID.randomUUID();

        DictionaryType entity = new DictionaryType();
        entity.setDictionaryTypeId(id);
        entity.setCode("MILITARY_STATUS");
        entity.setName("Военный статус");

        DictionaryTypeResponse expected = new DictionaryTypeResponse();
        expected.setDictionaryTypeId(id);
        expected.setCode("MILITARY_STATUS");
        expected.setName("Военный статус");

        when(dictionaryTypeRepository.findById(id))
                .thenReturn(Optional.of(entity));

        when(dictionaryTypeMapper.toResponse(entity))
                .thenReturn(expected);

        DictionaryTypeResponse result =
                dictionaryService.getDictionaryTypeById(id);

        assertEquals(expected, result);

        verify(dictionaryTypeRepository).findById(id);
        verify(dictionaryTypeMapper).toResponse(entity);
    }

    @Test
    void shouldThrowExceptionWhenDictionaryTypeNotFound() {
        UUID id = UUID.randomUUID();

        when(dictionaryTypeRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> dictionaryService.getDictionaryTypeById(id)
        );

        verify(dictionaryTypeRepository).findById(id);
        verify(dictionaryTypeMapper, never()).toResponse(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldGetDictionaryValuesByType() {
        UUID dictionaryTypeId = UUID.randomUUID();

        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setDictionaryTypeId(dictionaryTypeId);
        dictionaryType.setCode("MILITARY_STATUS");
        dictionaryType.setName("Военный статус");

        DictionaryValue first = new DictionaryValue();
        first.setDictionaryValueId(UUID.randomUUID());
        first.setDictionaryType(dictionaryType);
        first.setCode("MOBILIZED");
        first.setLabel("Мобилизованный");

        DictionaryValue second = new DictionaryValue();
        second.setDictionaryValueId(UUID.randomUUID());
        second.setDictionaryType(dictionaryType);
        second.setCode("CONTRACT");
        second.setLabel("Контрактник");

        DictionaryTypeResponse typeResponse =
                new DictionaryTypeResponse();

        typeResponse.setDictionaryTypeId(dictionaryTypeId);
        typeResponse.setCode("MILITARY_STATUS");
        typeResponse.setName("Военный статус");

        DictionaryValueResponse firstResponse =
                new DictionaryValueResponse();

        firstResponse.setDictionaryValueId(
                first.getDictionaryValueId()
        );
        firstResponse.setDictionaryTypeId(dictionaryTypeId);
        firstResponse.setCode("MOBILIZED");
        firstResponse.setLabel("Мобилизованный");

        DictionaryValueResponse secondResponse =
                new DictionaryValueResponse();

        secondResponse.setDictionaryValueId(
                second.getDictionaryValueId()
        );
        secondResponse.setDictionaryTypeId(dictionaryTypeId);
        secondResponse.setCode("CONTRACT");
        secondResponse.setLabel("Контрактник");

        when(dictionaryTypeRepository.findById(dictionaryTypeId))
                .thenReturn(Optional.of(dictionaryType));

        when(dictionaryValueRepository
                .findAllByDictionaryType_DictionaryTypeId(dictionaryTypeId))
                .thenReturn(List.of(first, second));

        when(dictionaryValueMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(dictionaryValueMapper.toResponse(second))
                .thenReturn(secondResponse);

        List<DictionaryValueResponse> result =
                dictionaryService.getDictionaryValues(dictionaryTypeId);

        assertEquals(2, result.size());
        assertEquals(firstResponse, result.get(0));
        assertEquals(secondResponse, result.get(1));

        verify(dictionaryTypeRepository).findById(dictionaryTypeId);

        verify(dictionaryValueRepository)
                .findAllByDictionaryType_DictionaryTypeId(dictionaryTypeId);

        verify(dictionaryValueMapper).toResponse(first);
        verify(dictionaryValueMapper).toResponse(second);
    }

    @Test
    void shouldThrowExceptionWhenDictionaryTypeNotFoundWhileGettingValues() {
        UUID dictionaryTypeId = UUID.randomUUID();

        when(dictionaryTypeRepository.findById(dictionaryTypeId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> dictionaryService.getDictionaryValues(
                        dictionaryTypeId
                )
        );

        verify(dictionaryTypeRepository).findById(dictionaryTypeId);

        verify(
                dictionaryValueRepository,
                never()
        ).findAllByDictionaryType_DictionaryTypeId(dictionaryTypeId);
    }

    @Test
    void shouldGetDictionaryValueById() {
        UUID id = UUID.randomUUID();

        DictionaryValue entity = new DictionaryValue();
        entity.setDictionaryValueId(id);
        entity.setCode("MOBILIZED");
        entity.setLabel("Мобилизованный");

        DictionaryValueResponse expected =
                new DictionaryValueResponse();

        expected.setDictionaryValueId(id);
        expected.setCode("MOBILIZED");
        expected.setLabel("Мобилизованный");

        when(dictionaryValueRepository.findById(id))
                .thenReturn(Optional.of(entity));

        when(dictionaryValueMapper.toResponse(entity))
                .thenReturn(expected);

        DictionaryValueResponse result =
                dictionaryService.getDictionaryValueById(id);

        assertEquals(expected, result);

        verify(dictionaryValueRepository).findById(id);
        verify(dictionaryValueMapper).toResponse(entity);
    }

    @Test
    void shouldThrowExceptionWhenDictionaryValueNotFound() {
        UUID id = UUID.randomUUID();

        when(dictionaryValueRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> dictionaryService.getDictionaryValueById(id)
        );

        verify(dictionaryValueRepository).findById(id);
        verify(dictionaryValueMapper, never()).toResponse(
                org.mockito.ArgumentMatchers.any()
        );
    }
}