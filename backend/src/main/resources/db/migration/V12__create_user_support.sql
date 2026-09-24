CREATE TABLE user_support (
                              user_support_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              user_id UUID NOT NULL,
                              support_id UUID NOT NULL,
                              status_id UUID NOT NULL,
                              selected_for_action BOOLEAN NOT NULL DEFAULT FALSE,
                              checked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              submitted_at TIMESTAMP,
                              received_at TIMESTAMP,
                              note TEXT,

                              CONSTRAINT fk_user_support_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES "user"(user_id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_user_support_support
                                  FOREIGN KEY (support_id)
                                      REFERENCES support_measure(support_id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT fk_user_support_status
                                  FOREIGN KEY (status_id)
                                      REFERENCES dictionary_value(dictionary_value_id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT uq_user_support_user_support
                                  UNIQUE (user_id, support_id)
);

CREATE INDEX idx_user_support_user
    ON user_support(user_id);

CREATE INDEX idx_user_support_support
    ON user_support(support_id);

CREATE INDEX idx_user_support_status
    ON user_support(status_id);