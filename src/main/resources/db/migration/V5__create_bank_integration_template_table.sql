CREATE TABLE tb_bank_integration_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    bank_id BIGINT NOT NULL,
    version VARCHAR(50) NOT NULL,
    steps_json TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_bank_integration_template_bank
        FOREIGN KEY (bank_id) REFERENCES tb_bank(id)
);