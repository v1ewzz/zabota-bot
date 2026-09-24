CREATE TABLE dictionary_type (
                                 dictionary_type_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 code VARCHAR(100) NOT NULL UNIQUE,
                                 name VARCHAR(255) NOT NULL
);