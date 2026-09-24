package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.SupportNpa;
import ru.zabota.bot.entity.id.SupportNpaId;

/*
 * Репозиторий для работы с сущностью SupportNpa.
 *
 * Использует составной идентификатор SupportNpaId.
 */
public interface SupportNpaRepository extends JpaRepository<SupportNpa, SupportNpaId> {
}