package ru.zabota.bot.dto.support;

import java.time.LocalDate;
import java.util.UUID;

/*
 * Краткие сведения о НПА, связанные с мерой поддержки.
 *
 * Предназначены для вывода пользователю названия документа
 * и прямой ссылки на официальный источник документа.
 */
public class NpaShortResponse {

    private UUID npaId;
    private String name;
    private String number;
    private LocalDate adoptionDate;
    private String officialUrl;

    public NpaShortResponse() {
    }

    public UUID getNpaId() { return npaId; }
    public void setNpaId(UUID npaId) { this.npaId = npaId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public LocalDate getAdoptionDate() { return adoptionDate; }
    public void setAdoptionDate(LocalDate adoptionDate) { this.adoptionDate = adoptionDate; }
    public String getOfficialUrl() { return officialUrl; }
    public void setOfficialUrl(String officialUrl) { this.officialUrl = officialUrl; }
}
