package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.UserChild;

import java.util.List;
import java.util.UUID;

/*
 * Репозиторий для работы с детьми пользователя.
 *
 * Помимо стандартного CRUD предоставляет операции
 * получения и удаления всех детей конкретного пользователя.
 */
public interface UserChildRepository extends JpaRepository<UserChild, UUID> {

    List<UserChild> findAllByUser_UserId(UUID userId);

    void deleteAllByUser_UserId(UUID userId);
}