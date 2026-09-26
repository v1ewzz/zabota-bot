-- V25__seed_additional_support_rules_and_npa_links.sql
--
-- Правила используют только параметры, которые уже понимает текущий
-- SupportMatchingService. Поэтому после V25 приложение продолжает работать
-- без изменения engine.
--
-- Для более точного подбора часть будущих параметров уже подготовлена
-- миграцией V21, но будет подключена к engine отдельным этапом.

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '7250278f-b26b-4958-ad55-4590a7e1e7c1',
    '6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '7250278f-b26b-4958-ad55-4590a7e1e7c1'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '7418697d-54ff-4786-a211-21b962ddb14d',
    '6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '7418697d-54ff-4786-a211-21b962ddb14d'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'c46360b0-d513-4646-9bc2-1a643470cc2e',
    '6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'c46360b0-d513-4646-9bc2-1a643470cc2e'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '091d6726-308a-4111-8baf-28c4e724da0c',
    '21ad0ed0-bb8a-48e5-b553-cd2a0475746e',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '091d6726-308a-4111-8baf-28c4e724da0c'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'a8fedb75-3ecc-4035-88b5-4f56b50ce3a2',
    '21ad0ed0-bb8a-48e5-b553-cd2a0475746e',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'a8fedb75-3ecc-4035-88b5-4f56b50ce3a2'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'f5fbbac7-ad11-429a-a68c-5a991759746b',
    '21ad0ed0-bb8a-48e5-b553-cd2a0475746e',
    'child.grade',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'BETWEEN'),
    'NUMBER',
    '1',
    '11',
    NULL,
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'f5fbbac7-ad11-429a-a68c-5a991759746b'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'efcb416d-c886-4613-9a9d-988f096f9d9e',
    '51b22e01-c91f-47de-a787-10e74dea0d42',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'efcb416d-c886-4613-9a9d-988f096f9d9e'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'd39b361b-7e46-4c85-8885-8eae9fa244fd',
    '51b22e01-c91f-47de-a787-10e74dea0d42',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'd39b361b-7e46-4c85-8885-8eae9fa244fd'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '6f5b3f56-a194-4124-b015-918148bb5e68',
    '51b22e01-c91f-47de-a787-10e74dea0d42',
    'child.education_level_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'SPO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '6f5b3f56-a194-4124-b015-918148bb5e68'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '7f352c8d-ed4c-4c9f-9b6e-6863855850b9',
    '51b22e01-c91f-47de-a787-10e74dea0d42',
    'child.full_time',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '7f352c8d-ed4c-4c9f-9b6e-6863855850b9'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '45828618-bc0b-4d32-8493-13d9ddb7533a',
    '96d888c5-96a8-45c9-9453-8ae1a7dd3de9',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '45828618-bc0b-4d32-8493-13d9ddb7533a'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'feb803ee-8327-427d-9d75-d7ebf2e3f61a',
    '96d888c5-96a8-45c9-9453-8ae1a7dd3de9',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'feb803ee-8327-427d-9d75-d7ebf2e3f61a'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '9ee99fd3-f378-497d-9657-b4a95c611474',
    '96d888c5-96a8-45c9-9453-8ae1a7dd3de9',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '9ee99fd3-f378-497d-9657-b4a95c611474'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '22a26b29-bcc3-4c94-8430-425f11e816f2',
    '31ed1407-34d5-4f3b-bb81-c9b0b8e79b02',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '22a26b29-bcc3-4c94-8430-425f11e816f2'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'd9058a04-4ddc-4005-993f-413cb9e000aa',
    '31ed1407-34d5-4f3b-bb81-c9b0b8e79b02',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'd9058a04-4ddc-4005-993f-413cb9e000aa'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'a172ba19-7b39-479b-85ce-997335b9328f',
    '31ed1407-34d5-4f3b-bb81-c9b0b8e79b02',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'a172ba19-7b39-479b-85ce-997335b9328f'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'ae207ad1-b4d0-4dd5-adc3-7944969a47d0',
    '967b1e4d-3a46-40b5-8db7-0b6eb941aaa6',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'ae207ad1-b4d0-4dd5-adc3-7944969a47d0'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '1695ae58-0d3a-4ed5-a5d8-c8eaa8c98faa',
    '967b1e4d-3a46-40b5-8db7-0b6eb941aaa6',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '1695ae58-0d3a-4ed5-a5d8-c8eaa8c98faa'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'a7854eb8-e8c7-49e9-a6d2-4f0115f3aa93',
    '967b1e4d-3a46-40b5-8db7-0b6eb941aaa6',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '7',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'a7854eb8-e8c7-49e9-a6d2-4f0115f3aa93'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '62a01544-2835-4e82-b892-a1b89312c5cb',
    '9c989821-f0fd-4420-8c7c-d2a7a3878e0a',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '62a01544-2835-4e82-b892-a1b89312c5cb'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '7fce9780-ac4a-4d9b-a039-a0fbc0ebf872',
    '9c989821-f0fd-4420-8c7c-d2a7a3878e0a',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '7fce9780-ac4a-4d9b-a039-a0fbc0ebf872'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '50c61f02-71ec-4e30-8fe6-713fbbfa9b01',
    '9c989821-f0fd-4420-8c7c-d2a7a3878e0a',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '18',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '50c61f02-71ec-4e30-8fe6-713fbbfa9b01'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '59fb32e2-eb46-416a-9ab8-d9d95fb91d0e',
    '6a366d9e-b0dd-4e06-a261-d09336f5adb3',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '59fb32e2-eb46-416a-9ab8-d9d95fb91d0e'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '91acc49d-8a96-4074-a615-caf5c4ea7f0c',
    '6a366d9e-b0dd-4e06-a261-d09336f5adb3',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '91acc49d-8a96-4074-a615-caf5c4ea7f0c'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '8bcaeec8-3f23-4aec-9e56-a178034a7299',
    '6a366d9e-b0dd-4e06-a261-d09336f5adb3',
    'child.grade',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'BETWEEN'),
    'NUMBER',
    '1',
    '6',
    NULL,
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '8bcaeec8-3f23-4aec-9e56-a178034a7299'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'ac413f22-0d14-4779-a45b-7b91aa0fe321',
    '2cbee12f-c2c0-40a6-8d90-f55812a54117',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'ac413f22-0d14-4779-a45b-7b91aa0fe321'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'abc30f76-c9d8-4d47-a35c-3a2486cf965f',
    '2cbee12f-c2c0-40a6-8d90-f55812a54117',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'abc30f76-c9d8-4d47-a35c-3a2486cf965f'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '3b753213-998d-477e-b5dd-cdbb1f04677d',
    '2cbee12f-c2c0-40a6-8d90-f55812a54117',
    'gasification_needed',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '3b753213-998d-477e-b5dd-cdbb1f04677d'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '133ae19d-fc87-4680-adc8-442801a4eae3',
    '9c537a64-ddc3-4b90-a45d-73f3d8415b51',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '133ae19d-fc87-4680-adc8-442801a4eae3'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'dfb17007-30dd-4fba-a268-ea50169fbeec',
    '9c537a64-ddc3-4b90-a45d-73f3d8415b51',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'dfb17007-30dd-4fba-a268-ea50169fbeec'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '3606ffd5-868f-4ed0-a85a-de00e05f3945',
    '9c537a64-ddc3-4b90-a45d-73f3d8415b51',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE,PARENT,CHILD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '3606ffd5-868f-4ed0-a85a-de00e05f3945'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'b322f7cc-2a61-4c29-b78e-0be559acc8b8',
    'f87a8eaa-985d-41a5-b5a0-d41b877fdce4',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'b322f7cc-2a61-4c29-b78e-0be559acc8b8'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'afe976b7-d627-4f6e-99b3-db32b14f0be1',
    'f87a8eaa-985d-41a5-b5a0-d41b877fdce4',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'afe976b7-d627-4f6e-99b3-db32b14f0be1'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '50c9ee49-002e-438e-92b7-4e012d6ecbae',
    'f87a8eaa-985d-41a5-b5a0-d41b877fdce4',
    'child.age',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'LT'),
    'NUMBER',
    NULL,
    NULL,
    '1',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '50c9ee49-002e-438e-92b7-4e012d6ecbae'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '0a14daa3-76ac-49b2-844c-9c2730c44bd9',
    '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '0a14daa3-76ac-49b2-844c-9c2730c44bd9'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '75a8231c-f2dc-43b6-9dc7-cc2c1e70d9df',
    '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '75a8231c-f2dc-43b6-9dc7-cc2c1e70d9df'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '1dcd1347-eed8-4069-b56a-f935afa63bd4',
    '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE,PARENT',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '1dcd1347-eed8-4069-b56a-f935afa63bd4'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '677e0574-0b6d-484e-a53f-17173564b57a',
    '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf',
    'disability_group_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'I,II',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '677e0574-0b6d-484e-a53f-17173564b57a'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'afd0f012-4014-4959-8f3f-8037e0f22fda',
    '2418d55d-ad70-4670-959f-7d44ed976aba',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'afd0f012-4014-4959-8f3f-8037e0f22fda'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '88dfaac5-f323-454f-b223-fa5fcec673b6',
    '2418d55d-ad70-4670-959f-7d44ed976aba',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '88dfaac5-f323-454f-b223-fa5fcec673b6'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'd016ff8b-71e1-4f46-a4fb-bf25c7fee8ca',
    '2418d55d-ad70-4670-959f-7d44ed976aba',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE,PARENT',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'd016ff8b-71e1-4f46-a4fb-bf25c7fee8ca'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'f4457204-f870-4152-945e-ae913ad9e44a',
    '2418d55d-ad70-4670-959f-7d44ed976aba',
    'disability_group_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'I,II',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'f4457204-f870-4152-945e-ae913ad9e44a'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '53f710bf-5e1d-4fe4-87bf-3cb8c2a5c5d5',
    '7ce13f15-016b-4a9c-aded-4a213aecdf23',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '53f710bf-5e1d-4fe4-87bf-3cb8c2a5c5d5'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'a8578481-ca76-41ea-b1d1-264b530ce419',
    '7ce13f15-016b-4a9c-aded-4a213aecdf23',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'a8578481-ca76-41ea-b1d1-264b530ce419'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'fadb34d5-fc5c-4d14-89ec-c2abc6abe08a',
    '7ce13f15-016b-4a9c-aded-4a213aecdf23',
    'child.disability',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'fadb34d5-fc5c-4d14-89ec-c2abc6abe08a'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'ad959b03-c8c0-42ee-9f2f-750319e650df',
    '393df4dc-d5a3-40f8-ae33-dfc30ab655ba',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'ad959b03-c8c0-42ee-9f2f-750319e650df'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '8453ad1c-de44-4dcf-924b-b9e4db61cdbb',
    '393df4dc-d5a3-40f8-ae33-dfc30ab655ba',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '8453ad1c-de44-4dcf-924b-b9e4db61cdbb'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '0942e1cd-5a7f-4a0f-bbdf-6da108cb2c8b',
    '393df4dc-d5a3-40f8-ae33-dfc30ab655ba',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE,PARENT,CHILD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '0942e1cd-5a7f-4a0f-bbdf-6da108cb2c8b'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '865942ef-f0af-4a64-9e10-c8ab3d1b3cef',
    '393df4dc-d5a3-40f8-ae33-dfc30ab655ba',
    'employment_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'UNEMPLOYED',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '865942ef-f0af-4a64-9e10-c8ab3d1b3cef'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '7ccda69b-de69-494d-b851-3f57a98d5152',
    '42e8aea5-ae19-4347-b934-78a0c99d445b',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '7ccda69b-de69-494d-b851-3f57a98d5152'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'fdc7d970-69e1-4de1-8ef6-a1ffcd0b86c3',
    '42e8aea5-ae19-4347-b934-78a0c99d445b',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'fdc7d970-69e1-4de1-8ef6-a1ffcd0b86c3'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '0deb25f6-715d-47f2-bdce-d624e1a5d86c',
    '42e8aea5-ae19-4347-b934-78a0c99d445b',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'SELF,WIFE,PARENT,CHILD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '0deb25f6-715d-47f2-bdce-d624e1a5d86c'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '49c64e83-466a-4520-86d2-19193c0dca8e',
    '42e8aea5-ae19-4347-b934-78a0c99d445b',
    'employment_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'SELF_EMPLOYED',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '49c64e83-466a-4520-86d2-19193c0dca8e'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '0987dab6-d829-43ab-b664-b5f7007b2aca',
    'ee3cb7cf-f8e5-4544-ac5d-b5adbe963bf6',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '0987dab6-d829-43ab-b664-b5f7007b2aca'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'e14d7957-65c3-4cc3-9d0f-67d007d6891b',
    'ee3cb7cf-f8e5-4544-ac5d-b5adbe963bf6',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'e14d7957-65c3-4cc3-9d0f-67d007d6891b'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '389fa6b8-cd37-42ac-be72-cc4575eb72a6',
    'ee3cb7cf-f8e5-4544-ac5d-b5adbe963bf6',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE,PARENT,CHILD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '389fa6b8-cd37-42ac-be72-cc4575eb72a6'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'd32f57d4-247c-47cc-a079-19686e81cb87',
    '94f093cf-5bb2-40af-8751-ea05b5ad6592',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'd32f57d4-247c-47cc-a079-19686e81cb87'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '576662e5-7d1a-4678-96c6-e1ae03553766',
    '94f093cf-5bb2-40af-8751-ea05b5ad6592',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '576662e5-7d1a-4678-96c6-e1ae03553766'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'c6c3739a-d7bd-446c-8af7-e00f3fdd1338',
    '94f093cf-5bb2-40af-8751-ea05b5ad6592',
    'employment_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'EMPLOYED',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'c6c3739a-d7bd-446c-8af7-e00f3fdd1338'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '30a47930-8500-4c99-82ce-396ed6dbb79d',
    '5db683e9-2e52-4ae6-873f-a9aef326e5a6',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'DECEASED_MILITARY',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '30a47930-8500-4c99-82ce-396ed6dbb79d'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'f845ac04-a927-4027-9f2b-8b6e2ddcafb1',
    '5db683e9-2e52-4ae6-873f-a9aef326e5a6',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE,PARENT,CHILD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'f845ac04-a927-4027-9f2b-8b6e2ddcafb1'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'f0198e8d-d8f5-4cc4-9f98-751eb01f2c07',
    'bf16f333-b279-4d68-8a61-bf44d15fdab0',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'f0198e8d-d8f5-4cc4-9f98-751eb01f2c07'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '51e2cbc7-0213-435c-89af-a91806ae03c8',
    'bf16f333-b279-4d68-8a61-bf44d15fdab0',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'WIFE,PARENT,CHILD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '51e2cbc7-0213-435c-89af-a91806ae03c8'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '9c012a70-4164-49bc-b905-2f0830c10231',
    'bf16f333-b279-4d68-8a61-bf44d15fdab0',
    'injury',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '9c012a70-4164-49bc-b905-2f0830c10231'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '8663b1b5-1f9f-4876-9461-6309ab557eaf',
    'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '8663b1b5-1f9f-4876-9461-6309ab557eaf'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '69832564-66c8-4464-837b-f523c5727ac0',
    'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'SELF',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '69832564-66c8-4464-837b-f523c5727ac0'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '7c6a2e5d-9947-4aff-a81d-d1ef4475cf2b',
    'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'VETERAN_BD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '7c6a2e5d-9947-4aff-a81d-d1ef4475cf2b'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '1cf556b7-4aef-4841-88e6-57e4923c9d26',
    'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d',
    'disability',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '1cf556b7-4aef-4841-88e6-57e4923c9d26'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '567e011c-425e-453c-8b8f-1b36eb37be01',
    '9a103f0d-7fdf-4670-a855-0faa9b60b2f6',
    'region_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'REFERENCE',
    NULL,
    NULL,
    'TATARSTAN',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '567e011c-425e-453c-8b8f-1b36eb37be01'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    'fdae0eed-e41a-4197-93e7-d2cf67b81108',
    '9a103f0d-7fdf-4670-a855-0faa9b60b2f6',
    'military_status_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'MOBILIZED,CONTRACT_SVO,SVO_PARTICIPANT,VOLUNTEER_SVO',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = 'fdae0eed-e41a-4197-93e7-d2cf67b81108'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '518b3b72-4eac-4e16-ad17-402eab546953',
    '9a103f0d-7fdf-4670-a855-0faa9b60b2f6',
    'family_relation_id',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'IN'),
    'REFERENCE',
    NULL,
    NULL,
    'SELF,WIFE,PARENT,CHILD',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '518b3b72-4eac-4e16-ad17-402eab546953'
);

INSERT INTO support_rule (
    rule_id, support_id, parameter, operator_id, value_type,
    value_from, value_to, value, condition_group, is_required, amount_override
)
SELECT
    '8439c674-02ab-48c9-9155-e9a3358305ca',
    '9a103f0d-7fdf-4670-a855-0faa9b60b2f6',
    'housing_problem',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'OPERATOR') AND code = 'EQUALS'),
    'BOOLEAN',
    NULL,
    NULL,
    'true',
    1,
    TRUE,
    NULL
WHERE NOT EXISTS (
    SELECT 1 FROM support_rule WHERE rule_id = '8439c674-02ab-48c9-9155-e9a3358305ca'
);


-- Нормативные связи дополнительных мер.

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe',
    '9002a50f-8605-4edc-b696-60585dc05da9',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe'
      AND npa_id = '9002a50f-8605-4edc-b696-60585dc05da9'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '21ad0ed0-bb8a-48e5-b553-cd2a0475746e',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '21ad0ed0-bb8a-48e5-b553-cd2a0475746e'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '51b22e01-c91f-47de-a787-10e74dea0d42',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '51b22e01-c91f-47de-a787-10e74dea0d42'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '96d888c5-96a8-45c9-9453-8ae1a7dd3de9',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '96d888c5-96a8-45c9-9453-8ae1a7dd3de9'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '31ed1407-34d5-4f3b-bb81-c9b0b8e79b02',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '31ed1407-34d5-4f3b-bb81-c9b0b8e79b02'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '967b1e4d-3a46-40b5-8db7-0b6eb941aaa6',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '967b1e4d-3a46-40b5-8db7-0b6eb941aaa6'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '9c989821-f0fd-4420-8c7c-d2a7a3878e0a',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '9c989821-f0fd-4420-8c7c-d2a7a3878e0a'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '6a366d9e-b0dd-4e06-a261-d09336f5adb3',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '6a366d9e-b0dd-4e06-a261-d09336f5adb3'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    'f87a8eaa-985d-41a5-b5a0-d41b877fdce4',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = 'f87a8eaa-985d-41a5-b5a0-d41b877fdce4'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '2418d55d-ad70-4670-959f-7d44ed976aba',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '2418d55d-ad70-4670-959f-7d44ed976aba'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '7ce13f15-016b-4a9c-aded-4a213aecdf23',
    '49e8a7a8-fa85-4fb7-b057-11af3dee12f7',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '7ce13f15-016b-4a9c-aded-4a213aecdf23'
      AND npa_id = '49e8a7a8-fa85-4fb7-b057-11af3dee12f7'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '9c537a64-ddc3-4b90-a45d-73f3d8415b51',
    '16447255-8078-494b-a5d6-acfce8ff7a41',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '9c537a64-ddc3-4b90-a45d-73f3d8415b51'
      AND npa_id = '16447255-8078-494b-a5d6-acfce8ff7a41'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '9c537a64-ddc3-4b90-a45d-73f3d8415b51',
    '00b93e8b-227d-4406-afe6-e4717bf3b10b',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'AMENDMENT')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '9c537a64-ddc3-4b90-a45d-73f3d8415b51'
      AND npa_id = '00b93e8b-227d-4406-afe6-e4717bf3b10b'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '94f093cf-5bb2-40af-8751-ea05b5ad6592',
    'c5d73e0c-6141-4435-9af0-2cbbf699e090',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '94f093cf-5bb2-40af-8751-ea05b5ad6592'
      AND npa_id = 'c5d73e0c-6141-4435-9af0-2cbbf699e090'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    '5db683e9-2e52-4ae6-873f-a9aef326e5a6',
    'c5d73e0c-6141-4435-9af0-2cbbf699e090',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = '5db683e9-2e52-4ae6-873f-a9aef326e5a6'
      AND npa_id = 'c5d73e0c-6141-4435-9af0-2cbbf699e090'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    'bf16f333-b279-4d68-8a61-bf44d15fdab0',
    'c5d73e0c-6141-4435-9af0-2cbbf699e090',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = 'bf16f333-b279-4d68-8a61-bf44d15fdab0'
      AND npa_id = 'c5d73e0c-6141-4435-9af0-2cbbf699e090'
);

INSERT INTO support_npa (support_id, npa_id, relation_type_id)
SELECT
    'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d',
    'bb9e56b6-a3a8-45ce-a993-0dd1119844c5',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'NPA_RELATION_TYPE') AND code = 'BASIS')
WHERE NOT EXISTS (
    SELECT 1
    FROM support_npa
    WHERE support_id = 'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d'
      AND npa_id = 'bb9e56b6-a3a8-45ce-a993-0dd1119844c5'
);

