package ru.zabota.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zabota.bot.entity.SupportMunicipality;
import ru.zabota.bot.entity.id.SupportMunicipalityId;

import java.util.List;
import java.util.UUID;

/*
 * Репозиторий связей между мерами социальной поддержки
 * и муниципальными образованиями.
 *
 * Используется для получения мер, доступных
 * на конкретном муниципальном образовании.
 */
public interface SupportMunicipalityRepository
        extends JpaRepository<SupportMunicipality, SupportMunicipalityId> {

    List<SupportMunicipality> findAllById_MunicipalityId(UUID municipalityId);
}