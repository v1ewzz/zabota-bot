CREATE TABLE support_municipality (
                                      support_id UUID NOT NULL,
                                      municipality_id UUID NOT NULL,
                                      valid_from DATE,
                                      valid_to DATE,

                                      CONSTRAINT pk_support_municipality
                                          PRIMARY KEY (support_id, municipality_id),

                                      CONSTRAINT fk_support_municipality_support
                                          FOREIGN KEY (support_id)
                                              REFERENCES support_measure(support_id)
                                              ON DELETE CASCADE,

                                      CONSTRAINT fk_support_municipality_municipality
                                          FOREIGN KEY (municipality_id)
                                              REFERENCES municipality(municipality_id)
                                              ON DELETE RESTRICT
);

CREATE INDEX idx_support_municipality_municipality
    ON support_municipality(municipality_id);