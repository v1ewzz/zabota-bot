package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.UserSupport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 * Репозиторий персональных результатов подбора.
 *
 * Позволяет получать все меры конкретного пользователя,
 * находить конкретную связь пользователя с мерой и обновлять
 * её статус без загрузки лишних персональных результатов.
 */
public interface UserSupportRepository extends JpaRepository<UserSupport, UUID> {

    List<UserSupport> findAllByUser_UserIdOrderByCheckedAtDesc(UUID userId);

    Optional<UserSupport> findByUser_UserIdAndUserSupportId(
            UUID userId,
            UUID userSupportId
    );

    Optional<UserSupport> findByUser_UserIdAndSupport_SupportId(
            UUID userId,
            UUID supportId
    );
}
