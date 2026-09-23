package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.Municipality;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 * Репозиторий для работы с сущностью Municipality.
 *
 * Помимо стандартного CRUD от JpaRepository предоставляет
 * специализированные запросы Spring Data JPA:
 *
 * - получение муниципалитетов конкретного региона;
 * - поиск муниципалитета по названию внутри региона;
 * - проверку существования муниципалитета с таким названием
 *   внутри конкретного региона.
 *
 * Запросы формируются Spring Data JPA автоматически
 * по именам методов.
 */
public interface MunicipalityRepository extends JpaRepository<Municipality, UUID> {

    List<Municipality> findAllByRegion_RegionId(UUID regionId);

    Optional<Municipality> findByNameIgnoreCaseAndRegion_RegionId(
            String name,
            UUID regionId
    );

    boolean existsByNameIgnoreCaseAndRegion_RegionId(
            String name,
            UUID regionId
    );
}