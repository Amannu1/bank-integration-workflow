create table TB_BANK(
    id BIGINT primary key auto_increment,
    name VARCHAR(100) NOT NULL,
    active boolean not null default false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO TB_BANK(NAME, ACTIVE, CREATED_AT) VALUES ('Itaú', false, now());
INSERT INTO TB_BANK(NAME, ACTIVE, CREATED_AT) VALUES ('SICREDI', true, now());