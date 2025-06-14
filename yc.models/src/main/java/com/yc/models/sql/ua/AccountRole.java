package com.yc.models.sql.ua;

import java.io.Serializable;

public class AccountRole implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 867666197482467194L;

    private Long id;

    private Account account;

    private Role role;

    private String status;

    public AccountRole()
    {

    }

    public AccountRole(Long id,
            Account account,
            Role role,
            String status)
    {
        this.id = id;
        this.account = account;
        this.role = role;
        this.status = status;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "AccountRole [id=" + id + ", account=" + account + ", role=" + role + ", status=" + status + "]";
    }
}
