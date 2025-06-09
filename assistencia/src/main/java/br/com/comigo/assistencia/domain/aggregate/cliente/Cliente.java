package br.com.comigo.assistencia.domain.aggregate.cliente;

import java.util.Date;
import java.util.List;

import br.com.comigo.common.model.records.Cpf;
import br.com.comigo.common.model.records.Email;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;

public class Cliente {
    private Long id;
    private String nome;
    private Cpf cpf;
    private Telefone telefone;
    private Telefone whatsapp;
    private Email email;
    private Endereco endereco;
    private Date dataNascimento;
    private List<Veiculo> veiculos;

    public Cliente(Long id, String nome, Cpf cpf, Telefone telefone, Telefone whatsapp, Email email, Endereco endereco,
            Date dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.whatsapp = whatsapp;
        this.email = email;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
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

    public Cpf getCpf() {
        return this.cpf;
    }

    public void setCpf(Cpf cpf) {
        this.cpf = cpf;
    }

    public Telefone getTelefone() {
        return this.telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public Telefone getWhatsapp() {
        return this.whatsapp;
    }

    public void setWhatsapp(Telefone whatsapp) {
        this.whatsapp = whatsapp;
    }

    public Email getEmail() {
        return this.email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Date getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }
}