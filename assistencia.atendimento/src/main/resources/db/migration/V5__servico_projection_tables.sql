
CREATE TABLE IF NOT EXISTS ASSISTENCIA_ES.RM_SERVICO (
    id UUID PRIMARY KEY,
    nome VARCHAR(64) NOT NULL,
    descricao TEXT,
    certificacao_iso BOOLEAN,
    status VARCHAR(32) NOT NULL,
    data_hora_disponibilizado TIMESTAMP NOT NULL,
    data_hora_indisponibilizado TIMESTAMP,
    data_hora_cancelado TIMESTAMP,
    data_hora_ajustado TIMESTAMP,
    version INTEGER NOT NULL
);