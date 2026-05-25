create table TB_ROLE (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    authority VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');