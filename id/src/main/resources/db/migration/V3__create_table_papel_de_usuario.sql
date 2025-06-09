CREATE TABLE id.papel_de_usuario (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    papel_id BIGINT NOT NULL,
    UNIQUE (usuario_id, papel_id),
    FOREIGN KEY (usuario_id) REFERENCES id.usuario (id) ON DELETE CASCADE,
    FOREIGN KEY (papel_id) REFERENCES id.papel (id) ON DELETE CASCADE
);