package br.com.comigo.id.domain.aggregate.papel;

import br.com.comigo.id.domain.util.StatusDePapel;

public class Papel {
    private Long id;
    private String nome;
    private StatusDePapel status;

    public Papel(Long id, String nome) {
        this.id = id;
        this.nome = nome;
        this.status = StatusDePapel.ATIVO;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusDePapel getStatus() {
        return this.status;
    }

    public void setStatus(StatusDePapel status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }

}