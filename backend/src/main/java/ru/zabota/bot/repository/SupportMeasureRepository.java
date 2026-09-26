package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.SupportMeasure;

import java.util.UUID;

/*
 * Репозиторий для работы с сущностью SupportMeasure.
 *
 * Использует стандартные возможности Spring Data JPA.
 */
public interface SupportMeasureRepository extends JpaRepository<SupportMeasure, UUID> {
}