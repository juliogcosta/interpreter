CREATE TABLE id.usuario (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    nome VARCHAR(256) NOT NULL,
    telefone_tipo VARCHAR(16),
    telefone_numero VARCHAR(16),
    email VARCHAR(256),
    status VARCHAR(16) NOT NULL
);