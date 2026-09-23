-- Храним фактически подобранную сумму меры,
-- включая amount_override из правила, чтобы личный кабинет
-- показывал тот же результат после повторного открытия.

ALTER TABLE user_support
    ADD COLUMN IF NOT EXISTS matched_amount DECIMAL(12, 2);
