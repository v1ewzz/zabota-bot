CREATE TABLE user_child (
                            child_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            user_id UUID NOT NULL,
                            birth_date DATE NOT NULL,
                            education_level_id UUID NOT NULL,
                            grade SMALLINT,
                            disability BOOLEAN NOT NULL DEFAULT FALSE,
                            disability_group_id UUID,
                            full_time BOOLEAN NOT NULL DEFAULT FALSE,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_user_child_user
                                FOREIGN KEY (user_id)
                                    REFERENCES "user"(user_id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_user_child_education_level
                                FOREIGN KEY (education_level_id)
                                    REFERENCES dictionary_value(dictionary_value_id)
                                    ON DELETE RESTRICT,

                            CONSTRAINT fk_user_child_disability_group
                                FOREIGN KEY (disability_group_id)
                                    REFERENCES dictionary_value(dictionary_value_id)
                                    ON DELETE RESTRICT
);

CREATE INDEX idx_user_child_user
    ON user_child(user_id);