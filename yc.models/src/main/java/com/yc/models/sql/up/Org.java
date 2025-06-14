package com.yc.models.sql.up;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Org implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -4584419226368079569L;
    
    static public enum EStatus {
        SUSPENDED, ACTIVE, CANCELED
    };
    
    private Long id = -1L;
    
    private String owner = null;
    
    private String name = null;
    
    private String alias = null;

    private String status = null;
    
    private String cardIdentifier = null;
    
    private String email = null;
    
    private String awsCredentials = null;
    
    private String logUser = null;
    
    private String logRole = null;
    
    private Long logCreatedAt = null;
    
    private Long logUpdatedAt = null;
    
    private Integer logVersion = null;
    
    private Set<AccountRoleOrg> accountRoleOrgs = null;
    
    public Org() {
        this.accountRoleOrgs = new HashSet<>();
    }
    
    public Org(String owner, String name, String alias) {
        this.name = name;
        this.owner = owner;
        this.alias = alias;
        
        this.accountRoleOrgs = new HashSet<>();
    }
    
    public Org(String owner, String name, String alias, String status) {
        this.name = name;
        this.owner = owner;
        this.alias = alias;
        this.status = status;
        
        this.accountRoleOrgs = new HashSet<>();
    }
    
    public Org(Long id) {
        this.id = id;
        
        this.accountRoleOrgs = new HashSet<>();
    }
    
    public Org(Long id, String owner, String name, String alias, String status, String email, String cardIdentifier,
        String awsCredentials, String logUser, String logRole, Long logCreatedAt, Long logUpdatedAt, Integer logVersion) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.alias = alias;
        this.cardIdentifier = cardIdentifier;
        this.awsCredentials = awsCredentials;
        this.status = status;
        this.email = email;
        this.logUser = logUser;
        this.logRole = logRole;
        this.logCreatedAt = logCreatedAt;
        this.logUpdatedAt = logUpdatedAt;
        this.logVersion = logVersion;
        
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
    
    public String getOwner()
    {
        return owner;
    }
    
    public void setOwner(String owner)
    {
        this.owner = owner;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getAlias()
    {
        return alias;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
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
    
    public String getCardIdentifier()
    {
        return cardIdentifier;
    }
    
    public void setCardIdentifier(String gatewayPaymentKey)
    {
        this.cardIdentifier = gatewayPaymentKey;
    }
    
    public String getAwsCredentials()
    {
        return awsCredentials;
    }
    
    public void setAwsCredentials(String awsCredentials)
    {
        this.awsCredentials = awsCredentials;
    }
    
    public Set<AccountRoleOrg> getAccountsRoles()
    {
        return accountRoleOrgs;
    }
    
    public void setAccountsRoles(Set<AccountRoleOrg> accountsRoles)
    {
        this.accountRoleOrgs = accountsRoles;
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
    
    public void setLogCreatedAt(Long logCreatedAt)
    {
        this.logCreatedAt = logCreatedAt;
    }
    
    public Long getLogUpdatedAt()
    {
        return logUpdatedAt;
    }
    
    public void setLogUpdatedAt(Long logUpdatedAt)
    {
        this.logUpdatedAt = logUpdatedAt;
    }
    
    public Integer getLogVersion()
    {
        return logVersion;
    }
    
    public void setLogVersion(Integer logVersion)
    {
        this.logVersion = logVersion;
    }
    
    @Override
    public String toString()
    {
        return "Org [id=" + id + ", owner=" + owner + ", name=" + name + ", alias=" + alias + ", cardIdentifier="
            + cardIdentifier + ", status=" + status + ", email=" + email + ", awsCredentials=" + awsCredentials
            + ", logUser=" + logUser + ", logRole=" + logRole + ", logCreatedAt=" + logCreatedAt + ", logUpdatedAt="
            + logUpdatedAt + ", logVersion=" + logVersion + ", accountsRoles=" + accountRoleOrgs + "]";
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
        Org other = (Org) obj;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (owner == null)
        {
            if (other.owner != null)
                return false;
        }
        else if (!owner.equals(other.owner))
            return false;
        return true;
    }
}
