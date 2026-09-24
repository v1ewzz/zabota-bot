package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.SupportRule;

import java.util.List;
import java.util.UUID;

/*
 * Репозиторий для работы с правилами мер социальной поддержки.
 *
 * Помимо стандартного CRUD предоставляет запросы для:
 *
 * - получения всех правил конкретной меры;
 * - получения правил конкретной меры с сортировкой по группам условий;
 * - получения правил конкретной группы условий;
 * - получения правил конкретного параметра.
 */
public interface SupportRuleRepository
        extends JpaRepository<SupportRule, UUID> {

    List<SupportRule> findAllBySupport_SupportId(
            UUID supportId
    );

    List<SupportRule> findAllBySupport_SupportIdOrderByConditionGroupAsc(
            UUID supportId
    );

    List<SupportRule> findAllBySupport_SupportIdAndConditionGroup(
            UUID supportId,
            Integer conditionGroup
    );

    List<SupportRule> findAllBySupport_SupportIdAndParameter(
            UUID supportId,
            String parameter
    );
}