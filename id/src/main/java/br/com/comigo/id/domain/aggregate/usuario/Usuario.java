package br.com.comigo.id.domain.aggregate.usuario;

import java.util.List;

import com.yc.core.common.model.records.Email;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.id.domain.util.StatusDeUsuario;

public class Usuario {
    private Long id;
    private String username;
    private String password;
    private String nome;
    private Telefone telefone;
    private Email email;
    private StatusDeUsuario status;
    private List<PapelDeUsuario> papelDeUsuarios;
    
    public Usuario(Long id, String username, String nome, Telefone telefone, Email email, StatusDeUsuario status) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Telefone getTelefone() {
        return this.telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public Email getEmail() {
        return this.email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public StatusDeUsuario getStatus() {
        return this.status;
    }

    public void setStatus(StatusDeUsuario status) {
        this.status = status;
    }

    public List<PapelDeUsuario> getPapelDeUsuarios() {
        return this.papelDeUsuarios;
    }

    public void setPapelDeUsuarios(List<PapelDeUsuario> veiculos) {
        this.papelDeUsuarios = veiculos;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", nome='" + getNome() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", email='" + getEmail() + "'" +
            ", status='" + getStatus() + "'" +
            ", veiculos='" + getPapelDeUsuarios() + "'" +
            "}";
    }

}