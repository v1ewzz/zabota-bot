CREATE TABLE municipality (
                              municipality_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              region_id UUID NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              district VARCHAR(255),
                              type_id UUID,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_municipality_region
                                  FOREIGN KEY (region_id)
                                      REFERENCES region(region_id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT fk_municipality_type
                                  FOREIGN KEY (type_id)
                                      REFERENCES dictionary_value(dictionary_value_id)
                                      ON DELETE RESTRICT
);

CREATE INDEX idx_municipality_region
    ON municipality(region_id);

CREATE INDEX idx_municipality_type
    ON municipality(type_id);