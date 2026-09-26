-- V26__normalize_support_source_urls.sql
--
-- Синхронизация официальных ссылок для всех существующих мер поддержки.
-- Источник URL: обе таблицы Excel, использованные при наполнении БД.
--
-- В V16 и V24 поле support_measure.action_url уже заполнялось, поэтому
-- миграция не создаёт новые меры и не меняет структуру БД. Она повторно
-- устанавливает action_url в единое нормализованное значение для всех 35 мер.
-- Это делает данные однозначными после расширения БД и исключает случайные
-- расхождения URL в существующих записях.

WITH source_urls (support_id, action_url) AS (
    VALUES
        -- Федеральные меры из V16
        ('d6d163b3-c7f8-48f0-b2fb-6414c8f8619d'::uuid, 'https://sfr.gov.ru/grazhdanam/semyam_s_detmi/edinoe_posobie'),
        ('42617a36-bca0-479e-8918-2f0303dc40af'::uuid, 'https://sfr.gov.ru/grazhdanam/socialnaya_podderzhka/semyam_s_detmi/beremennoj_zhene_voennosluzhashchego_prohodyashchego_'),
        ('3ed714db-7510-4a66-9234-2b48518e8984'::uuid, 'https://www.gosuslugi.ru/610789/1'),
        ('02035953-3078-40b6-a59c-f23a24271cc5'::uuid, 'https://publication.pravo.gov.ru/'),
        ('ac4975e3-126e-499b-b4d5-b0b5c408e5e7'::uuid, 'https://sfr.gov.ru/grazhdanam/pensionnoe_obespechenie/strahovaya_pensiya_po_sluchayu_poteri_kormilca'),
        ('8136d0c2-bb26-428e-8d99-ca3740f41b8f'::uuid, 'https://mintrud.gov.ru'),
        ('af721877-fe93-48b6-839c-a26e9717a37e'::uuid, 'https://www.sogaz.ru/'),
        ('6be55b34-3f90-4f69-9261-700f06ef274a'::uuid, 'https://rk.gov.ru/structure/8cc0cc8a-ff50-49d9-8857-0a5aae0d7a9a'),
        ('818936fa-e6da-4c1d-bb47-bf136371fdd4'::uuid, 'https://www.gosuslugi.ru'),
        ('3437ffae-f559-43b8-9ff2-fc939e0c6f0d'::uuid, 'https://www.gosuslugi.ru'),
        ('344f1960-1272-4307-bafd-e1c64619689a'::uuid, 'https://cdt-nv.gosuslugi.ru'),
        ('13646680-0935-4039-9568-7cc9b0238adf'::uuid, 'https://www.gosuslugi.ru'),
        ('12727507-84f0-47a9-aeb4-22f60d68c499'::uuid, 'http://www.pravo.gov.ru'),

        -- Дополнительные меры Татарстана из V24
        ('6d41b02a-53f1-4b51-b8eb-f92b4d4f8cbe'::uuid, 'https://pravo.tatarstan.ru/rus/file/npa/2022-10/1054377/npa_1054378.pdf'),
        ('21ad0ed0-bb8a-48e5-b553-cd2a0475746e'::uuid, 'https://pravo.tatarstan.ru/file/npa/2022-10/1061192/npa_1061193.pdf'),
        ('51b22e01-c91f-47de-a787-10e74dea0d42'::uuid, 'https://mon.tatarstan.ru/index.htm/news/2526738.htm'),
        ('96d888c5-96a8-45c9-9453-8ae1a7dd3de9'::uuid, 'https://base.garant.ru/406578411/'),
        ('31ed1407-34d5-4f3b-bb81-c9b0b8e79b02'::uuid, 'https://pravo.tatarstan.ru/file/npa/2022-10/1061192/npa_1061193.pdf'),
        ('967b1e4d-3a46-40b5-8db7-0b6eb941aaa6'::uuid, 'https://mon.tatarstan.ru/index.htm/news/2526738.htm'),
        ('9c989821-f0fd-4420-8c7c-d2a7a3878e0a'::uuid, 'https://mon.tatarstan.ru/index.htm/news/2526738.htm'),
        ('6a366d9e-b0dd-4e06-a261-d09336f5adb3'::uuid, 'https://mon.tatarstan.ru/index.htm/news/2526738.htm'),
        ('2cbee12f-c2c0-40a6-8d90-f55812a54117'::uuid, 'https://mtsz.tatarstan.ru/press/press_reliz.htm/press-release/9207559.htm'),
        ('9c537a64-ddc3-4b90-a45d-73f3d8415b51'::uuid, 'https://mtsz.tatarstan.ru/normativnie-dokumenti-predostavleniya-mer.htm'),
        ('f87a8eaa-985d-41a5-b5a0-d41b877fdce4'::uuid, 'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'),
        ('4d3f746d-8d9f-4c00-9ba4-62dce8194bcf'::uuid, 'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'),
        ('2418d55d-ad70-4670-959f-7d44ed976aba'::uuid, 'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'),
        ('7ce13f15-016b-4a9c-aded-4a213aecdf23'::uuid, 'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'),
        ('393df4dc-d5a3-40f8-ae33-dfc30ab655ba'::uuid, 'https://mtsz.tatarstan.ru/file/pub/pub_5003403.pdf'),
        ('42e8aea5-ae19-4347-b934-78a0c99d445b'::uuid, 'https://mtsz.tatarstan.ru/press/press_reliz.htm/press-release/10724879.htm'),
        ('ee3cb7cf-f8e5-4544-ac5d-b5adbe963bf6'::uuid, 'https://minjust.tatarstan.ru/index.htm/news/2509758.htm'),
        ('94f093cf-5bb2-40af-8751-ea05b5ad6592'::uuid, 'https://www.consultant.ru/document/cons_doc_LAW_18853/8d5c5c67b2afbb7cc8136b994a42aa5374a5b1ac/'),
        ('5db683e9-2e52-4ae6-873f-a9aef326e5a6'::uuid, 'https://www.consultant.ru/document/cons_doc_LAW_34683/ac98e98a7f06d32e7efc3643733e00e94c4fb1b6/'),
        ('bf16f333-b279-4d68-8a61-bf44d15fdab0'::uuid, 'https://www.consultant.ru/document/cons_doc_LAW_432078/f3facf40af2be972419bdfc73bf3bd6848531cab6/'),
        ('b7b3ad9d-57a3-48ca-a915-5d86d06b6d3d'::uuid, 'https://mtsz.tatarstan.ru/meri-sotsialnoy-podderzhki.htm'),
        ('9a103f0d-7fdf-4670-a855-0faa9b60b2f6'::uuid, 'https://mtsz.tatarstan.ru/press/press_reliz.htm/press-release/10656019.htm')
)
UPDATE support_measure sm
SET action_url = su.action_url
FROM source_urls su
WHERE sm.support_id = su.support_id
  AND sm.action_url IS DISTINCT FROM su.action_url;

-- Контрольная проверка: все 35 мер должны иметь корректный HTTP(S)-URL.
DO $$
DECLARE
    invalid_count INTEGER;
BEGIN
    SELECT COUNT(*)
      INTO invalid_count
      FROM support_measure
     WHERE action_url IS NULL
        OR action_url !~* '^https?://';

    IF invalid_count > 0 THEN
        RAISE EXCEPTION 'V26: найдено % мер без корректного action_url', invalid_count;
    END IF;
END $$;
