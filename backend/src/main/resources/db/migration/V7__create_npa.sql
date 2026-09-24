CREATE TABLE npa (
                     npa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                     name VARCHAR(500) NOT NULL,
                     npa_type_id UUID NOT NULL,
                     number VARCHAR(100) NOT NULL,
                     adoption_date DATE NOT NULL,
                     valid_from DATE,
                     valid_to DATE,
                     level_id UUID NOT NULL,
                     status_id UUID NOT NULL,
                     official_url TEXT NOT NULL,
                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                     CONSTRAINT fk_npa_type
                         FOREIGN KEY (npa_type_id)
                             REFERENCES dictionary_value(dictionary_value_id)
                             ON DELETE RESTRICT,

                     CONSTRAINT fk_npa_level
                         FOREIGN KEY (level_id)
                             REFERENCES dictionary_value(dictionary_value_id)
                             ON DELETE RESTRICT,

                     CONSTRAINT fk_npa_status
                         FOREIGN KEY (status_id)
                             REFERENCES dictionary_value(dictionary_value_id)
                             ON DELETE RESTRICT
);