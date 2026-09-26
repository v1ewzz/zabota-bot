-- V22__extend_dictionary_data.sql
--
-- Дополнительные справочные значения для расширенной БД и анкеты.

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT
    '5035fb82-e462-4be5-97d0-7db50fa841e8',
    dictionary_type_id,
    'SELF',
    'Сам военнослужащий',
    4,
    TRUE
FROM dictionary_type
WHERE code = 'FAMILY_RELATION'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT
    '84d7ba92-fe1e-46ec-a969-469a2e2f8bb3',
    dictionary_type_id,
    'MILITARY_MEMBER',
    'Военнослужащий',
    5,
    TRUE
FROM dictionary_type
WHERE code = 'RECIPIENT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

-- Новые типы мер.
INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT 'd446f91b-06e8-42d5-83c0-ed2c8fb37070', dictionary_type_id, 'FOOD_SUPPORT', 'Питание', 8, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '34be95e4-1d67-4db2-b682-83693e1790b2', dictionary_type_id, 'CULTURE_BENEFIT', 'Льгота в сфере культуры', 9, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '0f356d70-69e0-44c3-ba04-ea031ab5ec76', dictionary_type_id, 'CHILDCARE_BENEFIT', 'Льгота по дошкольному уходу', 10, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '28b42ce7-61ab-4b05-91d6-f8c1a7aecbb5', dictionary_type_id, 'SOCIAL_SERVICE', 'Социальное обслуживание', 11, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '9e4428fb-f4ca-4b16-bf15-7a60e292aaf4', dictionary_type_id, 'EMPLOYMENT_SUPPORT', 'Поддержка в сфере занятости', 12, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT 'f3551a7d-f1e1-4d42-8434-fd39a6ba42a6', dictionary_type_id, 'LEGAL_AID', 'Юридическая помощь', 13, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT 'ce44c243-723f-4732-a0aa-2ba15dc9a535', dictionary_type_id, 'LOAN_RELIEF', 'Льгота по кредитам и займам', 14, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '227dd8bb-d133-40f3-9298-767b1c24f310', dictionary_type_id, 'HOUSING_PROGRAM', 'Жилищная программа', 15, TRUE
FROM dictionary_type WHERE code = 'SUPPORT_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

-- Дополнительные каналы обращения.
INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '1be78add-71dd-4f7d-a79a-c298ce6cbfd0', dictionary_type_id, 'EMPLOYMENT_SERVICE', 'Служба занятости', 9, TRUE
FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '35169c18-c66f-46f5-8eaa-811039f6b7ad', dictionary_type_id, 'CULTURE_INSTITUTION', 'Учреждение культуры', 10, TRUE
FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '0aa7b0b4-947f-490e-b0ac-dd8847958d78', dictionary_type_id, 'SOCIAL_SERVICE_ORGANIZATION', 'Организация социального обслуживания', 11, TRUE
FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT 'f3211b49-65b1-465c-b0b9-fff4a5e9f623', dictionary_type_id, 'CREDITOR', 'Кредитор / банк / МФО', 12, TRUE
FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '696f596f-bde0-43e5-968a-fc40ccf36956', dictionary_type_id, 'HOUSING_AUTHORITY', 'Уполномоченный жилищный орган', 13, TRUE
FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

-- Типы региональных НПА.
INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT '407ded2d-eea7-4201-8972-9318d98380aa', dictionary_type_id, 'REGIONAL_RESOLUTION', 'Постановление субъекта РФ', 4, TRUE
FROM dictionary_type WHERE code = 'NPA_TYPE'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

-- Пол пользователя.
INSERT INTO dictionary_type (dictionary_type_id, code, name)
VALUES ('4bf0c987-ebf8-44ee-90f7-19d7405d8be3', 'SEX', 'Пол')
ON CONFLICT (code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
VALUES
    ('6b04b2f8-f391-4c7a-b0fa-575a235cc7d6', '4bf0c987-ebf8-44ee-90f7-19d7405d8be3', 'MALE', 'Мужской', 1, TRUE),
    ('21c00c5b-b2c8-43f5-be68-71576cc9093a', '4bf0c987-ebf8-44ee-90f7-19d7405d8be3', 'FEMALE', 'Женский', 2, TRUE)
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

-- Тип образовательной организации ребёнка.
INSERT INTO dictionary_type (dictionary_type_id, code, name)
VALUES ('c477bec0-01b6-4dfe-b57f-e71f72c06f3b', 'EDUCATION_ORGANIZATION_TYPE', 'Тип образовательной организации')
ON CONFLICT (code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
VALUES
    ('3a81f03e-1903-4045-8ed1-3810f0fc9cb3', 'c477bec0-01b6-4dfe-b57f-e71f72c06f3b', 'STATE', 'Государственная', 1, TRUE),
    ('d6438777-bade-4aa7-becd-82beeec4b448', 'c477bec0-01b6-4dfe-b57f-e71f72c06f3b', 'MUNICIPAL', 'Муниципальная', 2, TRUE),
    ('71e55022-d7fd-4cb6-9915-1bdd9f9177a1', 'c477bec0-01b6-4dfe-b57f-e71f72c06f3b', 'PRIVATE', 'Частная', 3, TRUE)
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

-- Категории вопросов для бесплатной юридической помощи.
INSERT INTO dictionary_type (dictionary_type_id, code, name)
VALUES ('3e73ed17-cb9c-45a9-b34b-c310bb7a214c', 'LEGAL_ISSUE_CATEGORY', 'Категория юридического вопроса')
ON CONFLICT (code) DO NOTHING;

INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
VALUES
    ('b51a01a1-07e6-4bc1-8a7c-fb3463d1732e', '3e73ed17-cb9c-45a9-b34b-c310bb7a214c', 'HOUSING', 'Жилищный вопрос', 1, TRUE),
    ('6a19582e-dcbe-4bb4-a13e-272bf8bf41bb', '3e73ed17-cb9c-45a9-b34b-c310bb7a214c', 'FAMILY', 'Семейный вопрос', 2, TRUE),
    ('675fb65c-b3dd-4ccc-aa8a-022b2c5477bf', '3e73ed17-cb9c-45a9-b34b-c310bb7a214c', 'LABOR', 'Трудовой вопрос', 3, TRUE),
    ('60fdb984-645c-4d5f-a87d-4d33e9459609', '3e73ed17-cb9c-45a9-b34b-c310bb7a214c', 'SOCIAL_SUPPORT', 'Социальная поддержка', 4, TRUE),
    ('9b006ae3-e67c-4aa8-87bb-cce4a2b22a39', '3e73ed17-cb9c-45a9-b34b-c310bb7a214c', 'OTHER', 'Иное', 5, TRUE)
ON CONFLICT (dictionary_type_id, code) DO NOTHING;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_user_sex'
    ) THEN
        ALTER TABLE "user"
            ADD CONSTRAINT fk_user_sex
            FOREIGN KEY (sex_id)
            REFERENCES dictionary_value(dictionary_value_id)
            ON DELETE RESTRICT;
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_user_legal_issue_category'
    ) THEN
        ALTER TABLE "user"
            ADD CONSTRAINT fk_user_legal_issue_category
            FOREIGN KEY (legal_issue_category_id)
            REFERENCES dictionary_value(dictionary_value_id)
            ON DELETE RESTRICT;
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_user_child_institution_type'
    ) THEN
        ALTER TABLE user_child
            ADD CONSTRAINT fk_user_child_institution_type
            FOREIGN KEY (institution_type_id)
            REFERENCES dictionary_value(dictionary_value_id)
            ON DELETE RESTRICT;
    END IF;
END $$;


INSERT INTO dictionary_value (
    dictionary_value_id, dictionary_type_id, code, label, sort_order, is_active
)
SELECT 'a16b9841-4bf0-4d1b-965e-29296365fbac', dictionary_type_id, 'EMPLOYER', 'Работодатель', 14, TRUE
FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL'
ON CONFLICT (dictionary_type_id, code) DO NOTHING;
