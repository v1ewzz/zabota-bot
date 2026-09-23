package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.Region;

import java.util.UUID;

/*
 * Репозиторий для работы с сущностью Region.
 *
 * Использует стандартные возможности Spring Data JPA.
 */
public interface RegionRepository extends JpaRepository<Region, UUID> {
}