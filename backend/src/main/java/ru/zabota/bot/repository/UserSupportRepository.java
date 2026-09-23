package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.UserSupport;

import java.util.UUID;

/*
 * Репозиторий для работы с сущностью UserSupport.
 *
 * Использует стандартные возможности Spring Data JPA.
 */
public interface UserSupportRepository extends JpaRepository<UserSupport, UUID> {
}