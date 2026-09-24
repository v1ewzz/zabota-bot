CREATE TABLE region (
                        region_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        name VARCHAR(255) NOT NULL,
                        code VARCHAR(50),
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);