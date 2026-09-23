package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.DictionaryValue;

import java.util.List;
import java.util.UUID;

/*
 * Репозиторий для работы с сущностью DictionaryValue.
 *
 * Использует стандартные возможности Spring Data JPA
 * и дополнительный метод для получения значений конкретного
 * типа справочника.
 */
public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, UUID> {

    List<DictionaryValue> findAllByDictionaryType_DictionaryTypeId(UUID dictionaryTypeId);
}