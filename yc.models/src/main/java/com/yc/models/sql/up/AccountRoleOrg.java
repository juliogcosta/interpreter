package com.yc.models.sql.up;

import java.io.Serializable;

public class AccountRoleOrg implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -769185057669637500L;
    
    static public enum EStatus {
        SUSPENDED, ACTIVE, CANCELED
    }
    
    private Long id = -1L;
    
    private Account account = null;
    
    private Role role = null;
    
    private Org org = null;
    
    private String accountStatus = null;
    
    private String orgStatus = null;
    
    public AccountRoleOrg() {
        
    }
    
    public AccountRoleOrg(Long id, Account account, Role role) {
        this.id = id;
        this.account = account;
        this.role = role;
    }
    
    public AccountRoleOrg(Long id, Account account, Role role, Org org) {
        this.id = id;
        this.account = account;
        this.role = role;
        this.org = org;
    }
    
    public AccountRoleOrg(Long id, Account account, String accountStatus, Role role, Org org, String orgStatus) {
        this.id = id;
        this.account = account;
        this.accountStatus = accountStatus;
        this.role = role;
        this.org = org;
        this.orgStatus = orgStatus;
    }
    
    public AccountRoleOrg(Long id, Account account, Role role, Org org, String orgStatus) {
        this.id = id;
        this.account = account;
        this.role = role;
        this.org = org;
        this.orgStatus = orgStatus;
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
    
    public Org getOrg()
    {
        return org;
    }
    
    public void setOrg(Org org)
    {
        this.org = org;
    }
    
    public String getAccountStatus()
    {
        return accountStatus;
    }
    
    public void setAccountStatus(String accountStatus)
    {
        this.accountStatus = accountStatus;
    }
    
    public String getOrgStatus()
    {
        return orgStatus;
    }
    
    public void setOrgStatus(String orgStatus)
    {
        this.orgStatus = orgStatus;
    }
    
    @Override
    public String toString()
    {
        return "AccountRole [id=" + id + ", account=" + account + ", role=" + role + ", org=" + org
            + ", accountStatus=" + accountStatus + ", orgStatus=" + orgStatus + "]";
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + ((org == null) ? 0 : org.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountRoleOrg other = (AccountRoleOrg) obj;
        if (account == null)
        {
            if (other.account != null)
                return false;
        }
        else if (!account.equals(other.account))
            return false;
        if (org == null)
        {
            if (other.org != null)
                return false;
        }
        else if (!org.equals(other.org))
            return false;
        if (role == null)
        {
            if (other.role != null)
                return false;
        }
        else if (!role.equals(other.role))
            return false;
        return true;
    }
}
