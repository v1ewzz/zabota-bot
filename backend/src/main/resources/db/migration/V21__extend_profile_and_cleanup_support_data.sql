-- V21__extend_profile_and_cleanup_support_data.sql
--
-- 1. Удаляет внутренний блок «Примечание для БД» из описаний уже загруженных мер.
-- 2. Расширяет профиль пользователя параметрами, которые понадобятся для
--    детальной анкеты и дальнейшего rules engine.
-- 3. Все новые поля nullable/с безопасными default, поэтому текущий код
--    не ломается на этапе миграции. Подключение этих полей к Java-моделям
--    выполняется отдельным изменением backend.

UPDATE support_measure
SET description = BTRIM(SPLIT_PART(description, E'\nПримечание для БД:', 1))
WHERE description LIKE E'%\nПримечание для БД:%';

ALTER TABLE "user"
    ADD COLUMN IF NOT EXISTS birth_date DATE,
    ADD COLUMN IF NOT EXISTS pregnancy_days SMALLINT,
    ADD COLUMN IF NOT EXISTS sex_id UUID,
    ADD COLUMN IF NOT EXISTS loan_exists BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS business_plan BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS job_seeker BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS social_service_need BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS serviceman_leave_start DATE,
    ADD COLUMN IF NOT EXISTS serviceman_leave_end DATE,
    ADD COLUMN IF NOT EXISTS legal_issue_category_id UUID;

ALTER TABLE user_child
    ADD COLUMN IF NOT EXISTS institution_type_id UUID;

CREATE INDEX IF NOT EXISTS idx_user_birth_date
    ON "user"(birth_date);

CREATE INDEX IF NOT EXISTS idx_user_sex
    ON "user"(sex_id);

CREATE INDEX IF NOT EXISTS idx_user_legal_issue_category
    ON "user"(legal_issue_category_id);

CREATE INDEX IF NOT EXISTS idx_user_child_institution_type
    ON user_child(institution_type_id);
