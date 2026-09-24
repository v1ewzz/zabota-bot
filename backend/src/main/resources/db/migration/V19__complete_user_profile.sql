-- Добавляет данные, которые вводятся пользователем на стартовом экране,
-- и фиксирует рабочий муниципалитет для текущего MVP.

ALTER TABLE "user"
    ADD COLUMN IF NOT EXISTS first_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS last_name VARCHAR(100);

UPDATE "user"
SET first_name = COALESCE(NULLIF(BTRIM(first_name), ''), 'Пользователь'),
    last_name = COALESCE(NULLIF(BTRIM(last_name), ''), 'Без фамилии')
WHERE first_name IS NULL
   OR BTRIM(first_name) = ''
   OR last_name IS NULL
   OR BTRIM(last_name) = '';

ALTER TABLE "user"
    ALTER COLUMN first_name SET NOT NULL,
    ALTER COLUMN last_name SET NOT NULL;

INSERT INTO municipality (
    municipality_id,
    region_id,
    name,
    district,
    type_id
)
SELECT
    '5b7e9c2a-3a34-4d8f-9c5f-2e7a1b6d4f20',
    '2cfa3160-48ad-48e8-9a71-7d7ca7dbdca0',
    'Казань',
    NULL,
    'f31be1d1-fc2b-4d83-b6c5-f74b9597cd45'
WHERE EXISTS (
    SELECT 1
    FROM region
    WHERE region_id = '2cfa3160-48ad-48e8-9a71-7d7ca7dbdca0'
)
AND NOT EXISTS (
    SELECT 1
    FROM municipality
    WHERE municipality_id = '5b7e9c2a-3a34-4d8f-9c5f-2e7a1b6d4f20'
);

CREATE INDEX IF NOT EXISTS idx_user_first_name
    ON "user"(first_name);

CREATE INDEX IF NOT EXISTS idx_user_last_name
    ON "user"(last_name);
