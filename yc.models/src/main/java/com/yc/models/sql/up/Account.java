package com.yc.models.sql.up;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Account implements Serializable
{
    private static final long serialVersionUID = 5376589565640881300L;

    static public enum EStatus { SUSPENDED, ACTIVE, CANCELED, PENDING }

    private Long id = -1L;

    private String username = null;

    private String password = null;

    private String name = null;

    private String email = null;

    private String status = null;

    private String endRua = null;

    private String endNumero = null;

    private String endComplemento = null;

    private String endBairro = null;

    private String endCidade = null;

    private String endUf = null;

    private String endPais = null;

    private String endCep = null;

    private String logUser = null;

    private String logRole = null;

    private Long logCreatedAt = null;

    private Long logUpdatedAt = null;

    private Integer logVersion = null;


    private String oldPassword = null;

    private String schemas = null;


    private Set<AccountRoleOrg> accountRoleOrgs = null;

    public Account() 
    {
        this.accountRoleOrgs = new HashSet<>();
    }

    public Account(Long id) 
    {
        this.id = id;

        this.accountRoleOrgs = new HashSet<>();
    }

    public Account(Long id, 
            String username, 
            String status) 
    {
        this.id = id;
        this.username = username;
        this.status = status;

        this.accountRoleOrgs = new HashSet<>();
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
            Integer logVersion) 
    {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
        this.logUser = logUser;
        this.logRole = logRole;
        this.logCreatedAt = logCreatedAt;
        this.logUpdatedAt = logUpdatedAt;
        this.logVersion = logVersion;

        this.accountRoleOrgs = new HashSet<>();
    }

    public Account(Long id, 
            String username, 
            String password, 
            String name,
            String email, 
            String status, 
            String endRua, 
            String endNumero, 
            String endComplemento,
            String endBairro, 
            String endCidade, 
            String endUf, 
            String endPais, 
            String endCep, 
            String logUser,
            String logRole,
            Long logCreatedAt, 
            Long logUpdatedAt) 
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.status = status;
        this.endRua = endRua;
        this.endNumero = endNumero;
        this.endComplemento = endComplemento;
        this.endBairro = endBairro;
        this.endCidade = endCidade;
        this.endUf = endUf;
        this.endPais = endPais;
        this.endCep = endCep;
        this.logUser = logUser;
        this.logRole = logRole;
        this.logCreatedAt = logCreatedAt;
        this.logUpdatedAt = logUpdatedAt;

        this.accountRoleOrgs = new HashSet<>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Set<AccountRoleOrg> getAccountRoleOrgs()
    {
        return accountRoleOrgs;
    }

    public void setAccountRoleOrgs(Set<AccountRoleOrg> accountsRoleOrgs)
    {
        this.accountRoleOrgs = accountsRoleOrgs;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getOldPassword()
    {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getLogUser()
    {
        return logUser;
    }

    public void setLogUser(String logUser)
    {
        this.logUser = logUser;
    }

    public String getLogRole()
    {
        return logRole;
    }

    public void setLogRole(String logRole)
    {
        this.logRole = logRole;
    }

    public Long getLogCreatedAt()
    {
        return logCreatedAt;
    }

    public void setLogCreatedAt(Long createdAt)
    {
        this.logCreatedAt = createdAt;
    }

    public Integer getLogVersion()
    {
        return logVersion;
    }

    public void setLogVersion(Integer version)
    {
        this.logVersion = version;
    }

    public Long getLogUpdatedAt()
    {
        return logUpdatedAt;
    }

    public void setLogUpdatedAt(Long updatedAt)
    {
        this.logUpdatedAt = updatedAt;
    }

    public String getEndRua()
    {
        return endRua;
    }

    public void setEndRua(String endRua)
    {
        this.endRua = endRua;
    }

    public String getEndNumero()
    {
        return endNumero;
    }

    public void setEndNumero(String endNumero)
    {
        this.endNumero = endNumero;
    }

    public String getEndBairro()
    {
        return endBairro;
    }

    public void setEndBairro(String endBairro)
    {
        this.endBairro = endBairro;
    }

    public String getEndComplemento()
    {
        return endComplemento;
    }

    public void setEndComplemento(String endComplemento)
    {
        this.endComplemento = endComplemento;
    }

    public String getEndCidade()
    {
        return endCidade;
    }

    public void setEndCidade(String endCidade)
    {
        this.endCidade = endCidade;
    }

    public String getEndUf()
    {
        return endUf;
    }

    public void setEndUf(String endUf)
    {
        this.endUf = endUf;
    }

    public String getEndPais()
    {
        return endPais;
    }

    public void setEndPais(String endPais)
    {
        this.endPais = endPais;
    }

    public String getEndCep()
    {
        return endCep;
    }

    public void setEndCep(String addrZip)
    {
        this.endCep = addrZip;
    }

    public String getSchemas()
    {
        return schemas;
    }

    public void setSchemas(String schemas)
    {
        this.schemas = schemas;
    }

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (username == null)
        {
            if (other.username != null)
                return false;
        }
        else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Account [getId()=" + getId() + ", getAccountRoleOrgs()=" + getAccountRoleOrgs() + ", getUsername()="
                + getUsername() + ", getName()=" + getName() + ", getPassword()=" + getPassword()
                + ", getOldPassword()=" + getOldPassword() + ", getEmail()=" + getEmail() + ", getStatus()="
                + getStatus() + ", getLogUser()=" + getLogUser() + ", getLogRole()=" + getLogRole()
                + ", getLogCreatedAt()=" + getLogCreatedAt() + ", getLogVersion()=" + getLogVersion()
                + ", getLogUpdatedAt()=" + getLogUpdatedAt() + ", getEndRua()=" + getEndRua() + ", getEndNumero()="
                + getEndNumero() + ", getEndBairro()=" + getEndBairro() + ", getEndComplemento()=" + getEndComplemento()
                + ", getEndCidade()=" + getEndCidade() + ", getEndUf()=" + getEndUf() + ", getEndPais()=" + getEndPais()
                + ", getEndCep()=" + getEndCep() + ", getSchemas()=" + getSchemas()
                + "]";
    }
}
