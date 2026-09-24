CREATE TABLE support_measure (
                                 support_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 name VARCHAR(500) NOT NULL,
                                 description TEXT NOT NULL,
                                 support_type_id UUID NOT NULL,
                                 level_id UUID NOT NULL,
                                 recipient_type_id UUID NOT NULL,
                                 application_required BOOLEAN NOT NULL,
                                 application_channel_id UUID,
                                 amount DECIMAL(12, 2),
                                 frequency_id UUID,
                                 documents TEXT,
                                 valid_from DATE,
                                 valid_to DATE,
                                 verification_status_id UUID NOT NULL,
                                 action_url TEXT,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_support_measure_type
                                     FOREIGN KEY (support_type_id)
                                         REFERENCES dictionary_value(dictionary_value_id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT fk_support_measure_level
                                     FOREIGN KEY (level_id)
                                         REFERENCES dictionary_value(dictionary_value_id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT fk_support_measure_recipient_type
                                     FOREIGN KEY (recipient_type_id)
                                         REFERENCES dictionary_value(dictionary_value_id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT fk_support_measure_application_channel
                                     FOREIGN KEY (application_channel_id)
                                         REFERENCES dictionary_value(dictionary_value_id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT fk_support_measure_frequency
                                     FOREIGN KEY (frequency_id)
                                         REFERENCES dictionary_value(dictionary_value_id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT fk_support_measure_verification_status
                                     FOREIGN KEY (verification_status_id)
                                         REFERENCES dictionary_value(dictionary_value_id)
                                         ON DELETE RESTRICT
);