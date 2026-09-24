CREATE TABLE support_rule (
                              rule_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              support_id UUID NOT NULL,
                              parameter VARCHAR(100) NOT NULL,
                              operator_id UUID NOT NULL,
                              value_type VARCHAR(20) NOT NULL,
                              value_from VARCHAR(255),
                              value_to VARCHAR(255),
                              value VARCHAR(255),
                              condition_group INTEGER NOT NULL,
                              is_required BOOLEAN NOT NULL DEFAULT FALSE,
                              amount_override DECIMAL(12, 2),
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_support_rule_support
                                  FOREIGN KEY (support_id)
                                      REFERENCES support_measure(support_id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_support_rule_operator
                                  FOREIGN KEY (operator_id)
                                      REFERENCES dictionary_value(dictionary_value_id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT chk_support_rule_value_type
                                  CHECK (
                                      value_type IN (
                                                     'NUMBER',
                                                     'DATE',
                                                     'TEXT',
                                                     'BOOLEAN',
                                                     'REFERENCE'
                                          )
                                      )
);

CREATE INDEX idx_support_rule_support_group
    ON support_rule(support_id, condition_group);