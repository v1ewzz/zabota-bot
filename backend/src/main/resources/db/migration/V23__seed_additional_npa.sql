-- V23__seed_additional_npa.sql
--
-- Нормативные акты для расширенных мер поддержки.
-- Для новых записей используется NEEDS_REVIEW до финальной сверки реквизитов.

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '9002a50f-8605-4edc-b696-60585dc05da9',
    $z$Постановление Кабинета Министров Республики Татарстан от 13.10.2022 № 1094$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_TYPE') AND code = 'REGIONAL_RESOLUTION'),
    '1094',
    '2022-10-13',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://pravo.tatarstan.ru/rus/file/npa/2022-10/1054377/npa_1054378.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM npa WHERE npa_id = '9002a50f-8605-4edc-b696-60585dc05da9'
);

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    $z$Постановление Кабинета Министров Республики Татарстан от 20.10.2022 № 1122$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_TYPE') AND code = 'REGIONAL_RESOLUTION'),
    '1122',
    '2022-10-20',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://pravo.tatarstan.ru/file/npa/2022-10/1061192/npa_1061193.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM npa WHERE npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '16447255-8078-494b-a5d6-acfce8ff7a41',
    $z$Постановление Кабинета Министров Республики Татарстан от 15.04.2022 № 357$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_TYPE') AND code = 'REGIONAL_RESOLUTION'),
    '357',
    '2022-04-15',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/normativnie-dokumenti-predostavleniya-mer.htm?pub_id=4483592'
WHERE NOT EXISTS (
    SELECT 1 FROM npa WHERE npa_id = '16447255-8078-494b-a5d6-acfce8ff7a41'
);

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '00b93e8b-227d-4406-afe6-e4717bf3b10b',
    $z$Постановление Кабинета Министров Республики Татарстан от 30.12.2024 № 1291$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_TYPE') AND code = 'REGIONAL_RESOLUTION'),
    '1291',
    '2024-12-30',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/file/pub/pub_4439621.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM npa WHERE npa_id = '00b93e8b-227d-4406-afe6-e4717bf3b10b'
);

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'c654be76-ea9b-4a42-9788-ccd01bf7bf46',
    $z$Федеральный закон от 07.10.2022 № 377-ФЗ$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_TYPE') AND code = 'FEDERAL_LAW'),
    '377-ФЗ',
    '2022-10-07',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://www.consultant.ru/document/cons_doc_LAW_428310/'
WHERE NOT EXISTS (
    SELECT 1 FROM npa WHERE npa_id = 'c654be76-ea9b-4a42-9788-ccd01bf7bf46'
);

