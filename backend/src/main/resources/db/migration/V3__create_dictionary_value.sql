CREATE TABLE dictionary_value (
                                  dictionary_value_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  dictionary_type_id UUID NOT NULL,
                                  code VARCHAR(100) NOT NULL,
                                  label VARCHAR(255) NOT NULL,
                                  sort_order SMALLINT,
                                  is_active BOOLEAN NOT NULL DEFAULT TRUE,

                                  CONSTRAINT fk_dictionary_value_type
                                      FOREIGN KEY (dictionary_type_id)
                                          REFERENCES dictionary_type(dictionary_type_id)
                                          ON DELETE RESTRICT,

                                  CONSTRAINT uq_dictionary_value_type_code
                                      UNIQUE (dictionary_type_id, code)
);

CREATE INDEX idx_dictionary_value_type
    ON dictionary_value(dictionary_type_id);