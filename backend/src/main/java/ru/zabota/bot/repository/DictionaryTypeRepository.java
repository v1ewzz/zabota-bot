package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.DictionaryType;

import java.util.UUID;

/*
 * Репозиторий для работы с сущностью DictionaryType.
 *
 * Использует стандартные возможности Spring Data JPA.
 */
public interface DictionaryTypeRepository extends JpaRepository<DictionaryType, UUID> {
}