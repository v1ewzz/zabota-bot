package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.User;

import java.util.UUID;

/*
 * Репозиторий для работы с сущностью User.
 *
 * Использует стандартные возможности Spring Data JPA.
 */
public interface UserRepository extends JpaRepository<User, UUID> {
}