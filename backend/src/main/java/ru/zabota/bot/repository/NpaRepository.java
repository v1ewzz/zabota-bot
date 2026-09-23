package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.Npa;

import java.util.UUID;

/*
 * Репозиторий для работы с сущностью Npa.
 *
 * Использует стандартные возможности Spring Data JPA.
 */
public interface NpaRepository extends JpaRepository<Npa, UUID> {
}