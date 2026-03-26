create table TB_BANK(
    id BIGINT primary key auto_increment,
    name VARCHAR(100) NOT NULL,
    active boolean not null default false
);

INSERT INTO TB_BANK(NAME, ACTIVE) VALUES ('Itaú', false);
INSERT INTO TB_BANK(NAME, ACTIVE) VALUES ('SICREDI', true);