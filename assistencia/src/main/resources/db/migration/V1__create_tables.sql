
-- -------------------------------
-- AGREGADO Prestador:

CREATE TABLE assistencia.prestador (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL UNIQUE,
    telefone_tipo VARCHAR(15),
    telefone_numero VARCHAR(15),
    whatsapp_tipo VARCHAR(15),
    whatsapp_numero VARCHAR(15),
    email VARCHAR(255),
    endereco_tipo VARCHAR(255),
    endereco_logradouro VARCHAR(255),
    endereco_numero VARCHAR(10),
    endereco_complemento VARCHAR(255),
    endereco_bairro VARCHAR(255),
    endereco_cidade VARCHAR(255),
    endereco_estado VARCHAR(2),
    endereco_cep VARCHAR(10),
    tem_certificacao_iso BOOLEAN,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE assistencia.servico_de_prestador (
    id UUID PRIMARY KEY,
    prestador_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (prestador_id) REFERENCES assistencia.prestador (id) ON DELETE CASCADE
);

CREATE TABLE assistencia.item_de_servico_de_prestador (
	id BIGSERIAL PRIMARY KEY,
	nome VARCHAR(32) NOT NULL,
    unidade_de_medida VARCHAR(16),
    preco_unitario INT NOT NULL,
    servico_de_prestador_id UUID NOT NULL,
    FOREIGN KEY (servico_de_prestador_id) REFERENCES assistencia.servico_de_prestador (id) ON DELETE CASCADE
);


-- -----------------------------
-- AGREGADO Cliente:

CREATE TABLE assistencia.cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    telefone_tipo VARCHAR(15),
    telefone_numero VARCHAR(15),
    whatsapp_tipo VARCHAR(15),
    whatsapp_numero VARCHAR(15),
    email VARCHAR(255),
    endereco_tipo VARCHAR(255),
    endereco_logradouro VARCHAR(255),
    endereco_numero VARCHAR(10),
    endereco_complemento VARCHAR(255),
    endereco_bairro VARCHAR(255),
    endereco_cidade VARCHAR(255),
    endereco_estado VARCHAR(2),
    endereco_cep VARCHAR(10),
    data_nascimento DATE
);

CREATE TABLE assistencia.veiculo_de_cliente (
    id BIGSERIAL PRIMARY KEY,
    marca VARCHAR(255) NOT NULL,
    modelo VARCHAR(255) NOT NULL,
    placa VARCHAR(20) NOT NULL UNIQUE,
    validado BOOLEAN NOT NULL,
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES assistencia.cliente (id) ON DELETE CASCADE
);


