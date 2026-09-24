-- V16__seed_support_measures.sql
-- Меры поддержки из Excel «Федеральные НПА: льготы для семей с детьми участников СВО (2026)».
-- Для status проверки использовано NEEDS_REVIEW: исходный файл содержит рекомендацию сверить реквизиты и размеры перед загрузкой.

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    'd6d163b3-c7f8-48f0-b2fb-6414c8f8619d',
    'Единое пособие на детей (с учётом правил для семей участников СВО)',
    'Ежемесячная выплата на каждого ребёнка до 17 лет при комплексной оценке нуждаемости. Доход участника СВО (служба по мобилизации / по контракту в зоне СВО) за период участия не учитывается в среднедушевом доходе семьи
Получатели: Один из родителей / усыновитель / опекун ребёнка
Критерии из источника: Ребёнку менее 17 лет; среднедушевой доход ниже ПМ; имущественные критерии; «правило нулевого дохода»; доход мобилизованного / участника СВО исключается из расчёта
Размер / вид поддержки: 50%, 75% или 100% ПМ на ребёнка в регионе; ПМ ребёнка в Татарстане на 2026 г. — 15 615 ₽ (макс. 15 615 ₽/мес на ребёнка)
Куда обращаться: Госуслуги / СФР / МФЦ
Примечание для БД: child_age < 17; military_status IN (MOBILIZED, CONTRACT_SVO); exclude_svo_income = true; amount = PM_child × {0.5; 0.75; 1.0}',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'PARENT'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'GOSUSLUGI'),
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'MONTHLY'),
    'Сведения о составе семьи, доходах и имуществе (часть — межведомственно); документы о службе родителя — для исключения его дохода',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://sfr.gov.ru/grazhdanam/semyam_s_detmi/edinoe_posobie';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '42617a36-bca0-479e-8918-2f0303dc40af',
    'Единовременное пособие беременной жене военнослужащего',
    'Единовременная выплата жене военнослужащего, проходящего военную службу по призыву (в т.ч. по мобилизации), при сроке беременности от 180 дней
Получатели: Жена военнослужащего, состоящая в зарегистрированном браке
Критерии из источника: Срок беременности ≥ 180 дней; муж проходит военную службу по призыву / по мобилизации; обращение не позднее 6 месяцев после окончания службы
Размер / вид поддержки: 45 054,24 ₽ с 01.02.2026 (без районного коэффициента; базовый размер 14 000 ₽ с ежегодной индексацией)
Куда обращаться: СФР / МФЦ / Госуслуги
Примечание для БД: spouse = true; pregnancy_days >= 180; military_status IN (CONSCRIPT, MOBILIZED, CONTRACT_SVO); payment_type = ONE_TIME; amount_2026 = 45054.24',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'SPOUSE'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'SFR'),
    45054.24,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'ONE_TIME'),
    'Паспорт; свидетельство о браке; справка о беременности из медорганизации; справка о прохождении службы мужем (из части / военкомата)',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://sfr.gov.ru/grazhdanam/socialnaya_podderzhka/semyam_s_detmi/beremennoj_zhene_voennosluzhashchego_prohodyashchego_';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '3ed714db-7510-4a66-9234-2b48518e8984',
    'Ежемесячное пособие на ребёнка военнослужащего (до 3 лет)',
    'Ежемесячная выплата на каждого ребёнка военнослужащего в период его службы — до достижения ребёнком 3 лет (но не позднее дня окончания службы отцом)
Получатели: Мать / опекун / другой родственник, фактически осуществляющий уход за ребёнком
Критерии из источника: Ребёнку менее 3 лет; отец проходит службу по призыву (в т.ч. по мобилизации) или участвует в СВО; пособие назначается на каждого ребёнка отдельно
Размер / вид поддержки: 19 308,96 ₽/мес с 01.02.2026 (базовый размер 14 000 ₽ + индексация). Внимание: в исходной таблице Татарстана указано 22 205,30 ₽ — рекомендуется сверить источник
Куда обращаться: СФР / МФЦ / Госуслуги
Примечание для БД: child_age < 3; military_status IN (CONSCRIPT, MOBILIZED, CONTRACT_SVO); payment_type = MONTHLY; amount_2026 = 19308.96',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'PARENT'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'SFR'),
    19308.96,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'MONTHLY'),
    'Свидетельство о рождении ребёнка; справка из воинской части / военкомата о прохождении службы; реквизиты счёта',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://www.gosuslugi.ru/610789/1';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '02035953-3078-40b6-a59c-f23a24271cc5',
    'Пенсия по случаю потери кормильца детям погибших военнослужащих («военная» пенсия)',
    'Пенсия по СПК каждому нетрудоспособному члену семьи погибшего (умершего) военнослужащего; детям — до 18 лет, при очном обучении — до 23 лет
Получатели: Дети погибшего (умершего) военнослужащего; иные нетрудоспособные члены семьи — в равных долях
Критерии из источника: Гибель (смерть) вследствие военной травмы — 50% денежного довольствия; вследствие заболевания, полученного в период службы, — 40% довольствия; вид службы кормильца (контракт / призыв) влияет на порядок расчёта
Размер / вид поддержки: 50% или 40% денежного довольствия погибшего (с учётом установленных повышений и надбавок)
Куда обращаться: Пенсионный орган силового ведомства (по линии Минобороны — военный комиссариат / ЕРЦ МО)
Примечание для БД: beneficiary = child; rate = 0.5 (military_trauma) | 0.4 (service_disease); child_age < 18 OR (fulltime_student AND child_age < 23)',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'PENSION'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'MILITARY_COMMISSARIAT'),
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'MONTHLY'),
    'Свидетельство о смерти; справка о гибели вследствие военной травмы; документы о родстве; справка об очном обучении (для 18–23 лет)',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://publication.pravo.gov.ru/';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    'ac4975e3-126e-499b-b4d5-b0b5c408e5e7',
    'Страховая пенсия по случаю потери кормильца детям (с повышенной фиксированной выплатой)',
    'Страховая пенсия по СПК несовершеннолетним детям умершего кормильца, имевшего страховой стаж; детям, потерявшим обоих родителей или одинокую мать, — фиксированная выплата в двойном размере
Получатели: Дети до 18 лет; студенты очной формы — до 23 лет
Критерии из источника: Наличие страхового стажа у кормильца; круглые сироты / дети умершей одинокой матери — ФВ × 2; срок назначения — до 18 лет (до 23 лет при очном обучении)
Размер / вид поддержки: ФВ на 2026 г.: 4 792,35 ₽ (потеря одного кормильца) / 9 584,69 ₽ (круглые сироты, дети одинокой матери) + страховая часть по ИПК (1 балл = 156,76 ₽)
Куда обращаться: СФР / Госуслуги / МФЦ
Примечание для БД: beneficiary = child; fixed_payment_2026 = 4792.35 | 9584.69 (orphan); ipk_value_2026 = 156.76',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'PENSION'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'GOSUSLUGI'),
    4792.35,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'MONTHLY'),
    'Свидетельство о смерти; подтверждение родства; СНИЛС; сведения о стаже кормильца (межведомственно)',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://sfr.gov.ru/grazhdanam/pensionnoe_obespechenie/strahovaya_pensiya_po_sluchayu_poteri_kormilca';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '8136d0c2-bb26-428e-8d99-ca3740f41b8f',
    'Социальная пенсия по случаю потери кормильца детям (в т.ч. повышенная — семьям военнослужащих-срочников)',
    'Социальная пенсия детям при отсутствии права на страховую пенсию; детям военнослужащих по призыву, погибших вследствие военной травмы, — 200% расчётного размера, вследствие заболевания в период службы — 150%
Получатели: Дети до 18 лет; студенты очной формы — до 23 лет
Критерии из источника: Отсутствие права на страховую пенсию; для повышенного размера — кормилец проходил службу по призыву; причина гибели (военная травма / заболевание в период службы)
Размер / вид поддержки: На 2026 г.: при потере одного кормильца ≈ 9 424 ₽/мес; круглые сироты и дети одинокой матери ≈ 18 848 ₽; 200% / 150% расчётного размера — семьям военнослужащих-срочников
Куда обращаться: СФР / Госуслуги / МФЦ
Примечание для БД: beneficiary = child; pension_type = SOCIAL_SPK; multiplier = 2.0 (military_trauma) | 1.5 (service_disease) для детей срочников; base_2026 ≈ 9424',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'PENSION'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'GOSUSLUGI'),
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'MONTHLY'),
    'Свидетельство о смерти; подтверждение родства; справка о службе кормильца по призыву и о причине гибели',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://mintrud.gov.ru';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    'af721877-fe93-48b6-839c-a26e9717a37e',
    'Страховые выплаты по обязательному государственному страхованию жизни и здоровья военнослужащих',
    'Страховая сумма при гибели (смерти) военнослужащего (в т.ч. участника СВО) — в равных долях выгодоприобретателям; при ранении (увечье) — застрахованному. Дети — выгодоприобретатели первой очереди
Получатели: Дети военнослужащего (до 18 лет / до 23 лет при очном обучении) и иные выгодоприобретатели по ст. 2
Критерии из источника: Страховой случай (гибель / ранение) в период службы; дети до 18 лет (до 23 — очно); доли между выгодоприобретателями равные
Размер / вид поддержки: На 2026 г.: при гибели — 3 683 261,71 ₽ (с учётом индексации); тяжёлое увечье — ≈ 368 000 ₽; лёгкое увечье — ≈ 92 000 ₽
Куда обращаться: Документы оформляет воинская часть / военный комиссариат; страховщик — АО «СОГАЗ»
Примечание для БД: beneficiary = child (age < 18 | student < 23); death_payout_2026 = 3683261.71; split = EQUAL',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'INSURANCE_PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'MILITARY_UNIT'),
    3683261.71,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'ONE_TIME'),
    'Заявление выгодоприобретателя; свидетельство о смерти; документы о родстве; справка об обстоятельствах страхового случая',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://www.sogaz.ru/';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '6be55b34-3f90-4f69-9261-700f06ef274a',
    'Единовременное пособие членам семьи погибшего (умершего) военнослужащего',
    'Единовременное пособие в равных долях членам семьи погибшего (умершего) военнослужащего, в т.ч. участника СВО; дети — равноправные получатели
Получатели: Члены семьи погибшего: дети, супруга (супруг), родители — по закону
Критерии из источника: Гибель (смерть) военнослужащего в период службы либо вследствие военной травмы; общая сумма делится поровну на всех членов семьи
Размер / вид поддержки: 3 000 000 ₽ в равных долях (по закону). Примечание: дополнительно семьям погибших выплачивается 5 000 000 ₽ по Указу Президента РФ от 05.03.2022 № 98 (указы в настоящий перечень ФЗ не включены)
Куда обращаться: Воинская часть / военный комиссариат / ЕРЦ Минобороны
Примечание для БД: beneficiary = family_members_incl_children; amount_total = 3000000; split = EQUAL; extra_payment_under_decree = 5000000 (Указ № 98, вне перечня)',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'PAYMENT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'MILITARY_UNIT'),
    3000000,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'ONE_TIME'),
    'Документы о родстве; свидетельство о смерти; справка о гибели (из воинской части)',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://rk.gov.ru/structure/8cc0cc8a-ff50-49d9-8857-0a5aae0d7a9a';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '818936fa-e6da-4c1d-bb47-bf136371fdd4',
    'Отдельная квота при поступлении в вузы (бакалавриат / специалитет)',
    'Приём на обучение за счёт бюджета в пределах отдельной квоты — не менее 10% от объёма контрольных цифр приёма — при условии успешного прохождения вступительных испытаний
Получатели: Дети участников СВО (включая детей мобилизованных и добровольцев; усыновлённые и неродные дети — также)
Критерии из источника: Поступление на бакалавриат / специалитет; родитель — участник СВО (статус подтверждён документально); успешное прохождение вступительных испытаний
Размер / вид поддержки: Не менее 10% бюджетных мест по каждой специальности / направлению подготовки
Куда обращаться: Приёмная комиссия вуза / Госуслуги («Поступление в вуз онлайн»)
Примечание для БД: admission_quota_pct >= 10; education_level IN (BACHELOR, SPECIALIST); parent_status = SVO_PARTICIPANT; require_vi_pass = true',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'ADMISSION_PRIORITY'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'UNIVERSITY'),
    NULL,
    NULL,
    'Документ, подтверждающий участие родителя в СВО (справка военкомата / воинской части); стандартный пакет поступающего',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://www.gosuslugi.ru';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '3437ffae-f559-43b8-9ff2-fc939e0c6f0d',
    'Первоочередной / внеочередной приём детей в детские сады, школы и летние оздоровительные лагеря',
    'Право первоочередного и внеочередного приёма детей военнослужащих и участников СВО в государственные и муниципальные детские сады, школы и летние оздоровительные лагеря
Получатели: Дети военнослужащих и участников СВО
Критерии из источника: Ребёнок — член семьи военнослужащего / участника СВО; организация государственная / муниципальная
Размер / вид поддержки: Приоритет при зачислении (первоочередной / внеочередной порядок)
Куда обращаться: Образовательная организация / муниципальный орган управления образованием
Примечание для БД: admission_priority IN (FIRST, OUT_OF_TURN); org_type IN (KINDERGARTEN, SCHOOL, SUMMER_CAMP); effective_from = 2023-06-24',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'ADMISSION_PRIORITY'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    'Документ, подтверждающий участие родителя в СВО / прохождение военной службы; стандартный пакет для приёма',
    '2023-06-24',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://www.gosuslugi.ru';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '344f1960-1272-4307-bafd-e1c64619689a',
    'Перевод с платного обучения на бюджетное место (для участников СВО и их детей)',
    'Право перейти с платного обучения на бесплатное (вакантное бюджетное место) в вузе или колледже; перевод проводится два раза в год по итогам сессий
Получатели: Участники СВО — студенты; их дети — студенты
Критерии из источника: Наличие вакантного бюджетного места; отсутствие академических задолженностей и дисциплинарных взысканий; документ об участии (родителя) в СВО
Размер / вид поддержки: Перевод на бюджетное место (обучение за счёт бюджета)
Куда обращаться: Учебная часть / специальная комиссия образовательной организации
Примечание для БД: transfer_to_budget = true; requires = vacant_place AND no_debts AND no_discipline; frequency = 2/YEAR; applies_to = SVO_participant + their_children',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'EDUCATION_RIGHT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'EDUCATIONAL_ORGANIZATION'),
    NULL,
    NULL,
    'Заявление о переходе; документ, подтверждающий личное участие или участие родителя в СВО',
    '2023-09-01',
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://cdt-nv.gosuslugi.ru';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '13646680-0935-4039-9568-7cc9b0238adf',
    'Компенсация 50% расходов на оплату жилого помещения семьям ветеранов боевых действий (включая участников СВО)',
    'Компенсация 50% платы за наём и (или) платы за содержание жилого помещения — в том числе членам семьи ветерана, совместно с ним проживающим (дети — в их числе)
Получатели: Ветераны боевых действий (участники СВО) и совместно проживающие с ними члены семьи
Критерии из источника: Статус ветерана боевых действий у родителя; совместное проживание членов семьи; расчёт от регионального стандарта стоимости ЖКУ
Размер / вид поддержки: 50% расходов на оплату жилого помещения (наём, содержание жилого помещения, взнос на капремонт — для нетрудоспособных членов семьи)
Куда обращаться: Орган социальной защиты региона / МФЦ / Госуслуги (в Татарстане — Минтруд РТ)
Примечание для БД: parent_status = VETERAN_BD (SVO); compensation_rate = 0.5; recipients = veteran + cohabiting_family',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'COMPENSATION'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'FAMILY'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'SOCIAL_PROTECTION'),
    NULL,
    NULL,
    'Удостоверение ветерана боевых действий; документы о составе семьи и совместном проживании; реквизиты счёта (часть сведений — межведомственно)',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'https://www.gosuslugi.ru';

INSERT INTO support_measure (
    support_id, name, description,
    support_type_id, level_id, recipient_type_id,
    application_required, application_channel_id,
    amount, frequency_id, documents,
    valid_from, valid_to, verification_status_id, action_url
)
SELECT
    '12727507-84f0-47a9-aeb4-22f60d68c499',
    'Бесплатный проезд детей военнослужащего-контрактника к месту использования отпуска и обратно',
    'Право членов семьи военнослужащего, проходящего службу по контракту (включая участников СВО), на бесплатный проезд к месту использования отпуска и обратно — один раз в год; к месту лечения — по медицинским показаниям
Получатели: Дети (и другие члены семьи) военнослужащего, проходящего военную службу по контракту
Критерии из источника: Военная служба по контракту; направление следования — к месту отпуска на территории РФ (1 раз в год) / к месту лечения
Размер / вид поддержки: Безвозмездный проезд железнодорожным, воздушным, водным и автомобильным (кроме такси) транспортом
Куда обращаться: Воинская часть (подтверждение права и возмещение расходов)
Примечание для БД: benefit = FREE_TRAVEL; frequency = 1/YEAR (vacation); applies_to = contract_service_family; transport IN (RAIL, AIR, WATER, BUS)',
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '89602651-8ddb-451a-a7fd-b9511a76820d' AND code = 'TRAVEL_BENEFIT'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = 'a8183718-2539-4b7d-b5ff-3ef677edcb91' AND code = 'FEDERAL'),
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '6edf22a6-b1ac-48b9-86e4-838aa4519817' AND code = 'CHILD'),
    TRUE,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '64ed556d-51d8-463d-a347-ddc8bb718151' AND code = 'MILITARY_UNIT'),
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '5fe54eef-e309-449f-84d2-238ded12002e' AND code = 'YEARLY'),
    'Приказ (справка) об отпуске; проездные документы; справка о составе семьи',
    NULL,
    NULL,
    (SELECT dictionary_value_id FROM dictionary_value WHERE dictionary_type_id = '460c857f-1d82-4b2e-85e4-cbcc111e3744' AND code = 'NEEDS_REVIEW'),
    'http://www.pravo.gov.ru';
