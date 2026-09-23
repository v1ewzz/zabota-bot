package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.SupportMunicipality;
import ru.zabota.bot.entity.id.SupportMunicipalityId;

/*
 * Репозиторий для работы с сущностью SupportMunicipality.
 *
 * Использует составной идентификатор SupportMunicipalityId.
 */
public interface SupportMunicipalityRepository
        extends JpaRepository<SupportMunicipality, SupportMunicipalityId> {
}