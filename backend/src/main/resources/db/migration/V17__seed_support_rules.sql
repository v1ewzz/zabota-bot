-- V17__seed_support_rules.sql
-- Машиночитаемые части условий из колонки «Важное для БД».
-- В правила заносится только то, что уже представимо текущим SupportSearchRequest/SupportMatchingService.
-- Неиспользуемые пока критерии (например, 180 дней беременности, имущественные критерии, вакантное бюджетное место) остаются в description меры.

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'b4e10b9d-ed8f-474d-844c-65491132af4e',
    'd6d163b3-c7f8-48f0-b2fb-6414c8f8619d',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '43aaa6ca-37ea-49b1-8a27-93bfd4902309',
    'd6d163b3-c7f8-48f0-b2fb-6414c8f8619d',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'c7875685-4914-4667-891b-04e4d8485210',
    'd6d163b3-c7f8-48f0-b2fb-6414c8f8619d',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '17',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '97aef078-6b26-44a0-8edd-3373b035df69',
    '42617a36-bca0-479e-8918-2f0303dc40af',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '7a6cc76c-ad8e-41d5-973e-26c3cf747c3c',
    '42617a36-bca0-479e-8918-2f0303dc40af',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'daeeb9e2-6ff8-48bc-a8de-e40d3648fa13',
    '42617a36-bca0-479e-8918-2f0303dc40af',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'CONSCRIPT,MOBILIZED,CONTRACT_SVO',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'e61067c6-b983-425c-97e4-fc9b2351400c',
    '42617a36-bca0-479e-8918-2f0303dc40af',
    'pregnancy',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'ed5bee69-c4e4-4944-8b5e-272070e2e7b1',
    '3ed714db-7510-4a66-9234-2b48518e8984',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'CONSCRIPT,MOBILIZED,CONTRACT_SVO',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '76548305-925e-42bf-a72a-c9b909c33b02',
    '3ed714db-7510-4a66-9234-2b48518e8984',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '3',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '966d50fe-de8b-4416-bb1e-3296b88cfe28',
    '02035953-3078-40b6-a59c-f23a24271cc5',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '13a4524d-0c4f-4430-8446-d68524509b86',
    '02035953-3078-40b6-a59c-f23a24271cc5',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'a3e1041b-0a50-4a63-b5ab-109a32dfd9c3',
    '02035953-3078-40b6-a59c-f23a24271cc5',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'b1412ae5-56e8-43ec-bef9-0220661c6438',
    '02035953-3078-40b6-a59c-f23a24271cc5',
    'child.full_time',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'a97c520d-95d0-48a9-8d0b-f77c8d9c5bea',
    '02035953-3078-40b6-a59c-f23a24271cc5',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '23',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '81b6189a-5341-44f2-bc7f-5ab7acb27141',
    'ac4975e3-126e-499b-b4d5-b0b5c408e5e7',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '1a4d1213-ddf6-4524-8b5d-d9f73ea961ae',
    'ac4975e3-126e-499b-b4d5-b0b5c408e5e7',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '394912d4-5149-457d-b113-f6ac3c2e5801',
    'ac4975e3-126e-499b-b4d5-b0b5c408e5e7',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '0dbc90a4-d708-4cb1-bd14-440290238931',
    'ac4975e3-126e-499b-b4d5-b0b5c408e5e7',
    'child.full_time',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'cf3193de-ee7a-4b3e-a21f-105020e8163d',
    'ac4975e3-126e-499b-b4d5-b0b5c408e5e7',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '23',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'b91bf0d0-0eca-4578-b4f5-a0b74a5aafb8',
    '8136d0c2-bb26-428e-8d99-ca3740f41b8f',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '9ae90ff6-93d9-4d2e-ae34-885f5f62a1e2',
    '8136d0c2-bb26-428e-8d99-ca3740f41b8f',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '68bd7339-4e82-43e3-8401-31e2795dc1f6',
    '8136d0c2-bb26-428e-8d99-ca3740f41b8f',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '515ecc5b-62d7-4a45-85f5-c93ee931745d',
    '8136d0c2-bb26-428e-8d99-ca3740f41b8f',
    'child.full_time',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'e8908a84-3c56-45c7-9124-0034591732a3',
    '8136d0c2-bb26-428e-8d99-ca3740f41b8f',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '23',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '479ad850-da5c-4bec-b93e-a3f6339591da',
    'af721877-fe93-48b6-839c-a26e9717a37e',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'f7cd0dbe-0474-49fb-9462-01d7acf385ae',
    'af721877-fe93-48b6-839c-a26e9717a37e',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '53c737a8-c5cf-43a4-b24f-432477f9a8d8',
    'af721877-fe93-48b6-839c-a26e9717a37e',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '936336f0-f209-4883-85c2-958dafd6d354',
    'af721877-fe93-48b6-839c-a26e9717a37e',
    'injury',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    2,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '66cbae86-2339-4e8e-97d0-7596ab98f718',
    '6be55b34-3f90-4f69-9261-700f06ef274a',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '8e9d08f4-0870-4393-8da9-e9d7b0fcdd62',
    '818936fa-e6da-4c1d-bb47-bf136371fdd4',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '12158327-be42-4a3a-8a88-46f2b760dcd3',
    '818936fa-e6da-4c1d-bb47-bf136371fdd4',
    'child.education_level_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'BACHELOR,SPECIALIST',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '63fd409f-de16-4b62-a58c-5e137ff5a41e',
    '3437ffae-f559-43b8-9ff2-fc939e0c6f0d',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'CONSCRIPT,MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'e21691f2-94ac-4cd3-a5dd-2300c66ce27d',
    '3437ffae-f559-43b8-9ff2-fc939e0c6f0d',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'GTE'),
    'NUMBER',
    NULL,
    NULL,
    '0',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'a1b8625a-8086-43cb-84fa-f7a7c1659c54',
    '344f1960-1272-4307-bafd-e1c64619689a',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '9ae79035-28a5-4195-aa41-adf34c7d4f8a',
    '344f1960-1272-4307-bafd-e1c64619689a',
    'child.education_level_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'SPO,BACHELOR,SPECIALIST',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '13e40ef2-d7f2-4641-ad48-15fca3c5771b',
    '13646680-0935-4039-9568-7cc9b0238adf',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'VETERAN_BD',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'c02130cd-f715-4e42-a113-932d4b124304',
    '12727507-84f0-47a9-aeb4-22f60d68c499',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'CONTRACT_SVO',
    1,
    TRUE,
    NULL;

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'f1456727-4e81-4a96-b69b-348e99f03889',
    '12727507-84f0-47a9-aeb4-22f60d68c499',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'b71349de-ddca-428e-9893-d97f13597578' AND code = 'GTE'),
    'NUMBER',
    NULL,
    NULL,
    '0',
    1,
    TRUE,
    NULL;
