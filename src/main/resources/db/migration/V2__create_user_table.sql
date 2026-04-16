create table TB_USER(
    id BIGINT primary key auto_increment,
    name VARCHAR(100) NOT NULL,
    active boolean not null default false,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO tb_user (name, active, password, created_at) VALUES ('Alex', true, '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', now());
INSERT INTO tb_user (name, active, password, created_at) VALUES ('Maria', false, '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', now());