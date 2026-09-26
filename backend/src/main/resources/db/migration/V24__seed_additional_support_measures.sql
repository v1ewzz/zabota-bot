-- V24__seed_additional_support_measures.sql
--
-- Расширение базы данных дополнительными мерами из таблицы
-- «Меры поддержки семей военнослужащих Татарстан 2026».
--
-- Дубликаты уже существующих федеральных мер намеренно НЕ добавляются:
--   - единое пособие;
--   - пособие беременной жене;
--   - пособие на ребёнка военнослужащего;
--   - отдельная квота;
--   - перевод с платного на бюджет;
--   - федеральная компенсация жилья;
--   - федеральная образовательная льгота по приёму детей.
--
-- Также не добавлены пока три меры, требующие новых параметров rules engine:
--   - санаторно-курортные путёвки (возраст/пол/медицинские показания);
--   - приоритетное рассмотрение бизнес-плана;
--   - кредитные каникулы (наличие действующего кредита).
-- Эти меры будут добавлены после подключения соответствующих полей анкеты
-- к Java-модели и SupportMatchingService.

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe',
    $z$Единовременная выплата на ребёнка участника СВО$z$,
    $z$Что даёт: Единовременная выплата на каждого несовершеннолетнего ребёнка
Кто получает: Ребёнок / законный представитель
Основные критерии для алгоритма: Отец относится к установленной категории участника СВО; ребёнок не достиг 18 лет
Размер / вид поддержки: 20 000 ₽ на каждого ребёнка
Заявление: Да
Куда обращаться: Орган соцзащиты / электронный сервис РТ$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'GOSUSLUGI'),
    20000,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'FREQUENCY') AND code = 'ONE_TIME'),
    $d$Документы личности, родства и статуса; часть сведений может проверяться межведомственно$d$,
    '2022-10-13',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://pravo.tatarstan.ru/rus/file/npa/2022-10/1054377/npa_1054378.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '21ad0ed0-bb8a-48e5-b553-cd2a0475746e',
    $z$Бесплатное двухразовое горячее питание школьников$z$,
    $z$Что даёт: Бесплатный завтрак и обед
Кто получает: Ребёнок — член семьи участника СВО
Основные критерии для алгоритма: Обучение в 1–11 классе государственной образовательной организации РТ; семья относится к установленной категории
Размер / вид поддержки: Бесплатное 2-разовое питание
Заявление: Да/по порядку школы
Куда обращаться: Образовательная организация$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'FOOD_SUPPORT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    $d$Подтверждение обучения и статуса семьи; перечень документов устанавливает порядок$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://pravo.tatarstan.ru/file/npa/2022-10/1061192/npa_1061193.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '21ad0ed0-bb8a-48e5-b553-cd2a0475746e'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '51b22e01-c91f-47de-a787-10e74dea0d42',
    $z$Бесплатное одноразовое горячее питание студентам СПО$z$,
    $z$Что даёт: Бесплатное одноразовое горячее питание
Кто получает: Студент — член семьи участника СВО
Основные критерии для алгоритма: Очная форма; программы подготовки специалистов среднего звена; государственная профессиональная образовательная организация РТ
Размер / вид поддержки: Бесплатное 1-разовое питание
Заявление: Да
Куда обращаться: Колледж / техникум$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'FOOD_SUPPORT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    $d$Подтверждение статуса семьи и обучения$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mon.tatarstan.ru/index.htm/news/2526738.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '51b22e01-c91f-47de-a787-10e74dea0d42'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '96d888c5-96a8-45c9-9453-8ae1a7dd3de9',
    $z$Бесплатное посещение мероприятий государственных учреждений культуры$z$,
    $z$Что даёт: Бесплатное посещение мероприятий государственных учреждений культуры РТ
Кто получает: Ребёнок до 18 лет + один родитель/законный представитель
Основные критерии для алгоритма: Ребёнок — член семьи участника СВО; возраст до 18 лет
Размер / вид поддержки: Бесплатное посещение
Заявление: Уточняется у учреждения
Куда обращаться: Государственное учреждение культуры$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'CULTURE_BENEFIT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'CULTURE_INSTITUTION'),
    NULL,
    NULL,
    $d$Документы, подтверждающие статус; порядок подтверждения зависит от учреждения$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://base.garant.ru/406578411/'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '96d888c5-96a8-45c9-9453-8ae1a7dd3de9'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '31ed1407-34d5-4f3b-bb81-c9b0b8e79b02',
    $z$Первоочередное обеспечение путёвками в детские лагеря$z$,
    $z$Что даёт: Первоочередное обеспечение путёвками на отдых детей
Кто получает: Дети из семей участников СВО
Основные критерии для алгоритма: Возраст и порядок зависят от действующего порядка организации отдыха; мера применяется к установленной категории
Размер / вид поддержки: Путёвка на льготных/безвозмездных условиях по установленному порядку
Заявление: Да
Куда обращаться: Уполномоченный орган / образовательная организация / сервис записи$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EDUCATION_RIGHT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    $d$Подтверждение статуса семьи, возраста ребёнка и регистрации; точный пакет зависит от программы$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://pravo.tatarstan.ru/file/npa/2022-10/1061192/npa_1061193.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '31ed1407-34d5-4f3b-bb81-c9b0b8e79b02'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '967b1e4d-3a46-40b5-8db7-0b6eb941aaa6',
    $z$Освобождение от родительской платы за детский сад$z$,
    $z$Что даёт: Освобождение от платы за присмотр и уход
Кто получает: Ребёнок — член семьи участника СВО
Основные критерии для алгоритма: Ребёнок посещает соответствующую муниципальную/государственную дошкольную организацию
Размер / вид поддержки: Освобождение от родительской платы
Заявление: Да
Куда обращаться: ДОО / муниципальный орган образования$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'CHILDCARE_BENEFIT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    $d$Подтверждение статуса семьи и посещения ДОО$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mon.tatarstan.ru/index.htm/news/2526738.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '967b1e4d-3a46-40b5-8db7-0b6eb941aaa6'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '9c989821-f0fd-4420-8c7c-d2a7a3878e0a',
    $z$Бесплатные кружки и секции / дополнительное образование$z$,
    $z$Что даёт: Бесплатное дополнительное образование для детей соответствующих семей
Кто получает: Дети участников СВО
Основные критерии для алгоритма: Ребёнок относится к установленной категории; программа реализуется соответствующей государственной/муниципальной организацией
Размер / вид поддержки: Освобождение от платы / бесплатное посещение
Заявление: Да
Куда обращаться: Организация дополнительного образования$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EDUCATION_RIGHT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    $d$Подтверждение статуса и личности ребёнка$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mon.tatarstan.ru/index.htm/news/2526738.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '9c989821-f0fd-4420-8c7c-d2a7a3878e0a'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '6a366d9e-b0dd-4e06-a261-d09336f5adb3',
    $z$Первоочередное зачисление в группы продлённого дня и освобождение от платы$z$,
    $z$Что даёт: Приоритет при зачислении в ГПД и освобождение от платы за присмотр
Кто получает: Дети 1–6 классов из соответствующих семей
Основные критерии для алгоритма: Класс 1–6; ребёнок относится к установленной категории
Размер / вид поддержки: Приоритет + освобождение от платы
Заявление: Да
Куда обращаться: Школа$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EDUCATION_RIGHT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    $d$Подтверждение обучения и статуса семьи$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mon.tatarstan.ru/index.htm/news/2526738.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '6a366d9e-b0dd-4e06-a261-d09336f5adb3'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '2cbee12f-c2c0-40a6-8d90-f55812a54117',
    $z$Субсидия на проведение газа и внутридомовое газовое оборудование$z$,
    $z$Что даёт: Возмещение затрат на газификацию домовладения
Кто получает: Семьи участников СВО
Основные критерии для алгоритма: Домовладение на территории РТ; заявитель/член семьи относится к установленной категории
Размер / вид поддержки: Фактические затраты, но не более 100 000 ₽ на домовладение
Заявление: Да
Куда обращаться: Орган соцзащиты / электронный сервис РТ$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'COMPENSATION'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_PROTECTION'),
    100000,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'FREQUENCY') AND code = 'ONE_TIME'),
    $d$Документы на домовладение, расходы, статус участника СВО$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/press/press_reliz.htm/press-release/9207559.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '2cbee12f-c2c0-40a6-8d90-f55812a54117'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '9c537a64-ddc3-4b90-a45d-73f3d8415b51',
    $z$Единовременная выплата семье погибшего участника СВО$z$,
    $z$Что даёт: Единовременная выплата членам семьи погибшего
Кто получает: Члены семьи погибшего участника СВО
Основные критерии для алгоритма: Смерть/гибель при обстоятельствах, предусмотренных порядком; наличие права у члена семьи
Размер / вид поддержки: 2 000 000 ₽ на семью по установленному порядку распределения
Заявление: Да
Куда обращаться: Минтруд РТ / соцзащита / единое заявление$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_PROTECTION'),
    2000000,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'FREQUENCY') AND code = 'ONE_TIME'),
    $d$Документы о смерти, родстве и статусе участника$d$,
    '2022-04-15',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/normativnie-dokumenti-predostavleniya-mer.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '9c537a64-ddc3-4b90-a45d-73f3d8415b51'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    'f87a8eaa-985d-41a5-b5a0-d41b877fdce4',
    $z$Подарочный комплект новорождённому$z$,
    $z$Что даёт: Комплект детских принадлежностей
Кто получает: Семьи участников СВО с новорождённым ребёнком
Основные критерии для алгоритма: Новорождённый; семья относится к установленной категории
Размер / вид поддержки: Подарочный комплект; без учёта среднедушевого дохода
Заявление: По установленному порядку
Куда обращаться: Орган соцзащиты / сервис РТ$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_PROTECTION'),
    NULL,
    NULL,
    $d$Сведения о рождении ребёнка и статусе семьи$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = 'f87a8eaa-985d-41a5-b5a0-d41b877fdce4'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf',
    $z$Бесплатные социальные услуги на дому$z$,
    $z$Что даёт: Бесплатное социальное обслуживание на дому
Кто получает: Пожилые члены семей и инвалиды I–II групп
Основные критерии для алгоритма: Возраст/инвалидность + принадлежность к семье участника СВО
Размер / вид поддержки: Бесплатное предоставление социальных услуг на дому
Заявление: Да
Куда обращаться: Орган соцобслуживания$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'SOCIAL_SERVICE'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_SERVICE_ORGANIZATION'),
    NULL,
    NULL,
    $d$Документы о возрасте/инвалидности и статусе семьи$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '4d3f746d-8d9f-4c00-9ba4-62dce8194bcf'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '2418d55d-ad70-4670-959f-7d44ed976aba',
    $z$Преимущественное право на социальное обслуживание в стационарных учреждениях$z$,
    $z$Что даёт: Приоритет при приёме в учреждения социального обслуживания
Кто получает: Члены семей участников СВО соответствующих категорий
Основные критерии для алгоритма: Наличие права на социальное обслуживание + принадлежность к семье участника
Размер / вид поддержки: Преимущественное право
Заявление: Да
Куда обращаться: Орган соцобслуживания$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'SOCIAL_SERVICE'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_SERVICE_ORGANIZATION'),
    NULL,
    NULL,
    $d$Документы о праве на обслуживание и статусе семьи$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '2418d55d-ad70-4670-959f-7d44ed976aba'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '7ce13f15-016b-4a9c-aded-4a213aecdf23',
    $z$Преимущественное право детей-инвалидов на реабилитацию$z$,
    $z$Что даёт: Приоритет при приёме в реабилитационные центры
Кто получает: Дети-инвалиды из соответствующих семей
Основные критерии для алгоритма: Инвалидность ребёнка + принадлежность к семье участника СВО
Размер / вид поддержки: Преимущественное право
Заявление: Да
Куда обращаться: Реабилитационный центр$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'SOCIAL_SERVICE'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_SERVICE_ORGANIZATION'),
    NULL,
    NULL,
    $d$Документы об инвалидности, родстве и статусе семьи$d$,
    '2022-10-20',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '7ce13f15-016b-4a9c-aded-4a213aecdf23'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '393df4dc-d5a3-40f8-ae33-dfc30ab655ba',
    $z$Сокращённый срок признания безработным$z$,
    $z$Что даёт: Решение о признании безработным не позднее 3 дней при невозможности предоставить подходящую работу
Кто получает: Члены семей участников СВО
Основные критерии для алгоритма: Регистрация в целях поиска подходящей работы; отсутствие возможности предложить подходящую работу
Размер / вид поддержки: Срок — не позднее 3 дней
Заявление: Да
Куда обращаться: Служба занятости$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EMPLOYMENT_SUPPORT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'EMPLOYMENT_SERVICE'),
    NULL,
    NULL,
    $d$Заявление и сведения о статусе семьи$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '393df4dc-d5a3-40f8-ae33-dfc30ab655ba'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '42e8aea5-ae19-4347-b934-78a0c99d445b',
    $z$Социальный контракт на предпринимательство / самозанятость$z$,
    $z$Что даёт: Государственная социальная помощь на предпринимательскую деятельность
Кто получает: Участники СВО и члены их семей
Основные критерии для алгоритма: Для участников СВО с 2026 г. не требуется оценка дохода семьи; остальные условия программы сохраняются
Размер / вид поддержки: До 350 000 ₽ для предпринимательства/самозанятости по указанной мере
Заявление: Да
Куда обращаться: Орган соцзащиты / электронный сервис РТ$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EMPLOYMENT_SUPPORT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_PROTECTION'),
    350000,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'FREQUENCY') AND code = 'ONE_TIME'),
    $d$Заявление, бизнес-план и документы по программе$d$,
    '2026-01-01',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/press/press_reliz.htm/press-release/10724879.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '42e8aea5-ae19-4347-b934-78a0c99d445b'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    'ee3cb7cf-f8e5-4544-ac5d-b5adbe963bf6',
    $z$Бесплатная юридическая помощь$z$,
    $z$Что даёт: Консультации и иные виды бесплатной юридической помощи по предусмотренным вопросам
Кто получает: Члены семей участников СВО
Основные критерии для алгоритма: Принадлежность к установленной категории; вопрос должен входить в перечень бесплатной помощи
Размер / вид поддержки: Бесплатная юридическая помощь
Заявление: Да
Куда обращаться: Уполномоченный орган / адвокатская палата / юридическая служба$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'LEGAL_AID'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_PROTECTION'),
    NULL,
    NULL,
    $d$Документ, подтверждающий статус и личность$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://minjust.tatarstan.ru/index.htm/news/2509758.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = 'ee3cb7cf-f8e5-4544-ac5d-b5adbe963bf6'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '94f093cf-5bb2-40af-8751-ea05b5ad6592',
    $z$Отпуск супруги одновременно с отпуском военнослужащего$z$,
    $z$Что даёт: Ежегодный отпуск супруги одновременно с отпуском военнослужащего
Кто получает: Супруга военнослужащего
Основные критерии для алгоритма: Супруга работает; отпуск военнослужащего; при превышении продолжительности — часть без сохранения зарплаты
Размер / вид поддержки: Отпуск в соответствующий период; превышение — без сохранения зарплаты
Заявление: Да
Куда обращаться: Работодатель$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EMPLOYMENT_SUPPORT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'SPOUSE'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'GOSUSLUGI'),
    NULL,
    NULL,
    $d$Заявление и подтверждение периода отпуска военнослужащего/брака$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://www.consultant.ru/document/cons_doc_LAW_18853/8d5c5c67b2afbb7cc8136b994a42aa5374a5b1ac/'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '94f093cf-5bb2-40af-8751-ea05b5ad6592'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '5db683e9-2e52-4ae6-873f-a9aef326e5a6',
    $z$Отпуск до 14 дней члену семьи погибшего$z$,
    $z$Что даёт: Отпуск без сохранения заработной платы
Кто получает: Родитель, супруг/супруга, ребёнок погибшего военнослужащего в предусмотренных законом случаях
Основные критерии для алгоритма: Подтверждён факт гибели и родство; соблюдение условий закона
Размер / вид поддержки: До 14 календарных дней в году
Заявление: Да
Куда обращаться: Работодатель$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EMPLOYMENT_SUPPORT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'GOSUSLUGI'),
    NULL,
    NULL,
    $d$Документы о родстве и статусе погибшего$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://www.consultant.ru/document/cons_doc_LAW_34683/ac98e98a7f06d32e7efc3643733e00e94c4fb1b6/'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '5db683e9-2e52-4ae6-873f-a9aef326e5a6'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    'bf16f333-b279-4d68-8a61-bf44d15fdab0',
    $z$Отпуск до 35 дней для ухода за раненым военнослужащим$z$,
    $z$Что даёт: Отпуск без сохранения заработной платы для ухода
Кто получает: Родитель, супруг/супруга, ребёнок военнослужащего
Основные критерии для алгоритма: Ранение/контузия/увечье/заболевание и медицинское заключение; родство
Размер / вид поддержки: До 35 календарных дней в году
Заявление: Да
Куда обращаться: Работодатель$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'EMPLOYMENT_SUPPORT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'GOSUSLUGI'),
    NULL,
    NULL,
    $d$Медицинское заключение и документы о родстве$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://www.consultant.ru/document/cons_doc_LAW_432078/f3facf40af2be972419bdfc73bf3bd6848531cab6/'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = 'bf16f333-b279-4d68-8a61-bf44d15fdab0'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d',
    $z$Компенсация расходов на оплату жилых помещений и коммунальных услуг инвалидам боевых действий$z$,
    $z$Что даёт: Компенсация расходов на жильё и коммунальные услуги
Кто получает: Инвалиды боевых действий
Основные критерии для алгоритма: Наличие статуса инвалида боевых действий
Размер / вид поддержки: Компенсация по установленным правилам
Заявление: Да
Куда обращаться: Органы соцзащиты$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'COMPENSATION'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'PARENT'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'SOCIAL_PROTECTION'),
    NULL,
    NULL,
    $d$Документы об инвалидности, статусе и расходах$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/meri-sotsialnoy-podderzhki.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = 'b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d'
);

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '9a103f0d-7fdf-4670-a855-0faa9b60b2f6',
    $z$Жилищная программа: приоритетное получение жилья по социальной ипотеке$z$,
    $z$Что даёт: Приоритетное участие в программе социальной ипотеки
Кто получает: Участники СВО и их семьи
Основные критерии для алгоритма: Соответствие условиям жилищной программы; состав семьи и жилищные условия
Размер / вид поддержки: Жильё по социальной ипотеке в приоритетном порядке
Заявление: Да
Куда обращаться: Уполномоченный жилищный орган / программа социмотеки$z$,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'SUPPORT_TYPE') AND code = 'HOUSING_PROGRAM'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'ADMIN_LEVEL') AND code = 'REGIONAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'RECIPIENT_TYPE') AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'APPLICATION_CHANNEL') AND code = 'HOUSING_AUTHORITY'),
    NULL,
    NULL,
    $d$Документы о семье, жилье и статусе участника$d$,
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = (SELECT dictionary_type_id FROM dictionary_type WHERE code = 'VERIFICATION_STATUS') AND code = 'NEEDS_REVIEW'),
    'https://mtsz.tatarstan.ru/press/press_reliz.htm/press-release/10656019.htm'
WHERE NOT EXISTS (
    SELECT 1 FROM support_measure WHERE support_id = '9a103f0d-7fdf-4670-a855-0faa9b60b2f6'
);

