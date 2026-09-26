-- V15__seed_federal_npa.sql
-- Федеральные НПА из предоставленной таблицы.
-- STATUS = NEEDS_REVIEW, поскольку исходный Excel рекомендует сверить реквизиты и размеры перед загрузкой.

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '97f8b01a-005a-4288-8ed8-a92e4553eebb',
    'Федеральный закон от 19.05.1995 № 81-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '81-ФЗ',
    '1995-05-19',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '31941179-d213-4741-a1be-aaa984f031e8',
    'Постановление Правительства РФ от 16.12.2022 № 2330',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'GOVERNMENT_RESOLUTION'),
    '2330',
    '2022-12-16',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'ee75170a-14fb-4054-b1dd-17b4c29e6665',
    'Закон РФ от 12.02.1993 № 4468-1',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '4468-1',
    '1993-02-12',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '1ec15cf2-ef5a-4d42-bcee-1ca5a61eef5d',
    'Федеральный закон от 28.12.2013 № 400-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '400-ФЗ',
    '2013-12-28',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'dcb390be-89db-4f8b-a5f9-f37aba65cc94',
    'Федеральный закон от 15.12.2001 № 166-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '166-ФЗ',
    '2001-12-15',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    '9e93c00c-1bd7-4f51-91ce-bace0e91487a',
    'Федеральный закон от 28.03.1998 № 52-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '52-ФЗ',
    '1998-03-28',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'a6ea71c5-c051-4d9b-914e-c9f9a9244ab0',
    'Федеральный закон от 07.11.2011 № 306-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '306-ФЗ',
    '2011-11-07',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/document/0001201111100029';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'ba514d43-3c14-449e-aea1-86144c637cf3',
    'Федеральный закон от 29.12.2012 № 273-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '273-ФЗ',
    '2012-12-29',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/document/0001201212300007';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'b1609018-0934-4e50-a105-0eb1faedac4c',
    'Приказ Минобрнауки России от 09.08.2023 № 776',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'ORDER'),
    '776',
    '2023-08-09',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'bb9e56b6-a3a8-45ce-a993-0dd1119844c5',
    'Федеральный закон от 12.01.1995 № 5-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '5-ФЗ',
    '1995-01-12',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO npa (
    npa_id, name, npa_type_id, number, adoption_date,
    valid_from, valid_to, level_id, status_id, official_url
)
SELECT
    'c5d73e0c-6141-4435-9af0-2cbbf699e090',
    'Федеральный закон от 27.05.1998 № 76-ФЗ',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'ce961ece-4d1c-4b45-a8eb-3bb522a52671' AND code = 'FEDERAL_LAW'),
    '76-ФЗ',
    '1998-05-27',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '99ade21c-8b3d-48fe-8258-aeb71716fda0' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';
