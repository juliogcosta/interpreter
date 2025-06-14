package com.yc.models.sql.ua;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Account extends com.yc.models.sql.up.Account implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -6260964596511201738L;

    private String foneUm = "";
    private String foneDois = "";
    private String whatsapp = "";
    private String genero = "";
    private String sexo = "";
    private Date nascimento = null;
    private String cpf = "";
    private String pai = "";
    private String mae = "";
    private String endpRua = "";
    private String endpNumero = "";
    private String endpComplemento = "";
    private String endpBairro = ""; 
    private String endpCidade = "";
    private String endpUf = "";
    private String endpCep = "";
    private String endpPais = "";
    private String habilitacaoNumero = "";
    private Date habilitacaoValidade = null;
    private String rgNumero = "";
    private String rgOrgaoEmissor = "";

    private Set<AccountRole> accountsRoles = null;

    public Account()
    {
        accountsRoles = new HashSet<AccountRole>();
    }

    public Account(Long id, 
            String username, 
            String password, 
            String name, 
            String email, 
            String status, 
            String logUser, 
            String logRole, 
            Long logCreatedAt, 
            Long logUpdatedAt, 
            Integer logVersion,
            String foneUm,
            String foneDois,
            String whatsapp,
            String genero,
            String sexo,
            Date nascimento,
            String endRua,
            String endNumero,
            String endComplemento,
            String endBairro,
            String endCidade,
            String endUf,
            String endPais,
            String endCep,
            String cpf,
            String pai,
            String mae,
            String endpRua,
            String endpNumero,
            String endpComplemento,
            String endpBairro,
            String endpCidade,
            String endpUf,
            String endpPais,
            String endpCep,
            String habilitacaoNumero,
            Date habilitacaoValidade,
            String rgNumero,
            String rgOrgaoEmissor) 
    {
        super(id, 
                username, 
                password, 
                name,
                email, 
                status, 
                endRua, 
                endNumero, 
                endComplemento,
                endBairro, 
                endCidade, 
                endUf, 
                endPais, 
                endCep, 
                logUser,
                logRole,
                logCreatedAt, 
                logUpdatedAt);

        this.foneUm = foneUm;
        this.foneDois = foneDois;
        this.whatsapp = whatsapp;
        this.genero = genero;
        this.sexo = sexo;
        this.nascimento = nascimento;
        this.cpf = cpf;
        this.pai = pai;
        this.mae = mae;
        this.endpRua = endpRua;
        this.endpNumero = endpNumero;
        this.endpComplemento = endpComplemento;
        this.endpBairro = endpBairro;
        this.endpCidade = endpCidade;
        this.endpUf = endpUf;
        this.endpPais = endpPais;
        this.endpCep = endpCep;
        this.habilitacaoNumero = habilitacaoNumero;
        this.habilitacaoValidade = habilitacaoValidade;
        this.rgNumero = rgNumero;
        this.rgOrgaoEmissor = rgOrgaoEmissor;

        this.accountsRoles = new HashSet<AccountRole>();
    }

    public void setAccountRoles(Set<AccountRole> accountsRoles)
    {
        this.accountsRoles = accountsRoles;
    }

    public Set<AccountRole> getAccountRoles()
    {
        return this.accountsRoles;
    }

    public String getFoneUm()
    {
        return foneUm;
    }

    public void setFoneUm(String foneUm)
    {
        this.foneUm = foneUm;
    }

    public String getFoneDois()
    {
        return foneDois;
    }

    public void setFoneDois(String foneDois)
    {
        this.foneDois = foneDois;
    }

    public String getWhatsapp()
    {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp)
    {
        this.whatsapp = whatsapp;
    }

    public String getGenero()
    {
        return genero;
    }

    public void setGenero(String genero)
    {
        this.genero = genero;
    }

    public String getSexo()
    {
        return sexo;
    }

    public void setSexo(String sexo)
    {
        this.sexo = sexo;
    }

    public Date getNascimento()
    {
        return nascimento;
    }

    public void setNascimento(Date nascimento)
    {
        this.nascimento = nascimento;
    }

    public String getCpf()
    {
        return cpf;
    }

    public void setCpf(String cpf)
    {
        this.cpf = cpf;
    }

    public String getPai()
    {
        return pai;
    }

    public void setPai(String pai)
    {
        this.pai = pai;
    }

    public String getMae()
    {
        return mae;
    }

    public void setMae(String mae)
    {
        this.mae = mae;
    }

    public String getEndpRua()
    {
        return endpRua;
    }

    public void setEndpRua(String endpRua)
    {
        this.endpRua = endpRua;
    }

    public String getEndpNumero()
    {
        return endpNumero;
    }

    public void setEndpNumero(String endpNumero)
    {
        this.endpNumero = endpNumero;
    }

    public String getEndpComplemento()
    {
        return endpComplemento;
    }

    public void setEndpComplemento(String endpComplemento)
    {
        this.endpComplemento = endpComplemento;
    }

    public String getEndpBairro()
    {
        return endpBairro;
    }

    public void setEndpBairro(String endpBairro)
    {
        this.endpBairro = endpBairro;
    }

    public String getEndpCidade()
    {
        return endpCidade;
    }

    public void setEndpCidade(String endpCidade)
    {
        this.endpCidade = endpCidade;
    }

    public String getEndpUf()
    {
        return endpUf;
    }

    public void setEndpUf(String endpUf)
    {
        this.endpUf = endpUf;
    }

    public String getEndpPais()
    {
        return endpPais;
    }

    public void setEndpPais(String endpPais)
    {
        this.endpPais = endpPais;
    }

    public String getEndpCep()
    {
        return endpCep;
    }

    public void setEndpCep(String endpCep)
    {
        this.endpCep = endpCep;
    }

    public String getHabilitacaoNumero()
    {
        return habilitacaoNumero;
    }

    public void setHabilitacaoNumero(String habilitacaoNumero)
    {
        this.habilitacaoNumero = habilitacaoNumero;
    }

    public Date getHabilitacaoValidade()
    {
        return habilitacaoValidade;
    }

    public void setHabilitacaoValidade(Date habilitacaoValidade)
    {
        this.habilitacaoValidade = habilitacaoValidade;
    }

    public String getRgNumero()
    {
        return rgNumero;
    }

    public void setRgNumero(String rgNumero)
    {
        this.rgNumero = rgNumero;
    }

    public String getRgOrgaoEmissor()
    {
        return rgOrgaoEmissor;
    }

    public void setRgOrgaoEmissor(String rgOrgaoEmissor)
    {
        this.rgOrgaoEmissor = rgOrgaoEmissor;
    }

    @Override
    public String toString()
    {
        return "Account [getAccountRoles()=" + getAccountRoles() + ", getFoneUm()=" + getFoneUm() + ", getFoneDois()="
                + getFoneDois() + ", getWhatsapp()=" + getWhatsapp() + ", getGenero()=" + getGenero() + ", getSexo()="
                + getSexo() + ", getNascimento()=" + getNascimento() + ", getCpf()=" + getCpf() + ", getPai()="
                + getPai() + ", getMae()=" + getMae() + ", getEndpRua()=" + getEndpRua() + ", getEndpNumero()="
                + getEndpNumero() + ", getEndpComplemento()=" + getEndpComplemento() + ", getEndpBairro()="
                + getEndpBairro() + ", getEndpCidade()=" + getEndpCidade() + ", getEndpUf()=" + getEndpUf()
                + ", getEndpPais()=" + getEndpPais() + ", getEndpCep()=" + getEndpCep() + ", getHabilitacaoNumero()="
                + getHabilitacaoNumero() + ", getHabilitacaoValidade()=" + getHabilitacaoValidade() + ", getRgNumero()="
                + getRgNumero() + ", getRgOrgaoEmissor()=" + getRgOrgaoEmissor() + ", getId()=" + getId()
                + ", getAccountRoleOrgs()=" + getAccountRoleOrgs() + ", getUsername()=" + getUsername() + ", getName()="
                + getName() + ", getPassword()=" + getPassword() + ", getOldPassword()=" + getOldPassword()
                + ", getEmail()=" + getEmail() + ", getStatus()=" + getStatus() + ", getLogUser()=" + getLogUser()
                + ", getLogRole()=" + getLogRole() + ", getLogCreatedAt()=" + getLogCreatedAt() + ", getLogVersion()="
                + getLogVersion() + ", getLogUpdatedAt()=" + getLogUpdatedAt() + ", getEndRua()=" + getEndRua()
                + ", getEndNumero()=" + getEndNumero() + ", getEndBairro()=" + getEndBairro() + ", getEndComplemento()="
                + getEndComplemento() + ", getEndCidade()=" + getEndCidade() + ", getEndUf()=" + getEndUf()
                + ", getEndPais()=" + getEndPais() + ", getEndCep()=" + getEndCep() + ", getSchemas()=" + getSchemas()
                + "]";
    }
    
}
