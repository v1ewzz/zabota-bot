CREATE TABLE support_npa (
                             support_id UUID NOT NULL,
                             npa_id UUID NOT NULL,
                             relation_type_id UUID NOT NULL,

                             CONSTRAINT pk_support_npa
                                 PRIMARY KEY (support_id, npa_id),

                             CONSTRAINT fk_support_npa_support
                                 FOREIGN KEY (support_id)
                                     REFERENCES support_measure(support_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_support_npa_npa
                                 FOREIGN KEY (npa_id)
                                     REFERENCES npa(npa_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_support_npa_relation_type
                                 FOREIGN KEY (relation_type_id)
                                     REFERENCES dictionary_value(dictionary_value_id)
                                     ON DELETE RESTRICT
);

CREATE INDEX idx_support_npa_npa
    ON support_npa(npa_id);

CREATE INDEX idx_support_npa_relation_type
    ON support_npa(relation_type_id);