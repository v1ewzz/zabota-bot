CREATE TABLE "user" (
                        user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        region_id UUID NOT NULL,
                        municipality_id UUID NOT NULL,
                        family_relation_id UUID,
                        military_status_id UUID,
                        pregnancy BOOLEAN,
                        injury BOOLEAN NOT NULL DEFAULT FALSE,
                        disability BOOLEAN NOT NULL DEFAULT FALSE,
                        disability_group_id UUID,
                        housing_problem BOOLEAN NOT NULL DEFAULT FALSE,
                        gasification_needed BOOLEAN NOT NULL DEFAULT FALSE,
                        employment_status_id UUID,
                        income_category_id UUID,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_user_region
                            FOREIGN KEY (region_id)
                                REFERENCES region(region_id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_user_municipality
                            FOREIGN KEY (municipality_id)
                                REFERENCES municipality(municipality_id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_user_family_relation
                            FOREIGN KEY (family_relation_id)
                                REFERENCES dictionary_value(dictionary_value_id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_user_military_status
                            FOREIGN KEY (military_status_id)
                                REFERENCES dictionary_value(dictionary_value_id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_user_disability_group
                            FOREIGN KEY (disability_group_id)
                                REFERENCES dictionary_value(dictionary_value_id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_user_employment_status
                            FOREIGN KEY (employment_status_id)
                                REFERENCES dictionary_value(dictionary_value_id)
                                ON DELETE RESTRICT,

                        CONSTRAINT fk_user_income_category
                            FOREIGN KEY (income_category_id)
                                REFERENCES dictionary_value(dictionary_value_id)
                                ON DELETE RESTRICT
);

CREATE INDEX idx_user_region
    ON "user"(region_id);

CREATE INDEX idx_user_municipality
    ON "user"(municipality_id);