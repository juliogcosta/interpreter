package com.yc.models.sql.up;

import java.io.Serializable;

public class Role implements Serializable
{
    private static final long serialVersionUID = 8523789719437617302L;

    public static final String PUBLIC = "PUBLIC";
    public static final String MASTER = "MASTER";
    public static final String ENGINEER = "ENGINEER";
    public static final String FINANCIAL = "FINANCIAL";
    public static final String ANALYST = "ANALYST";

    static public enum EStatus { SUSPENDED, ACTIVE, PENDING, CANCELED }

    private Long id = -1L;

    private String name = null;

    private String status = null;

    private String logUser = null;

    private String logRole = null;

    private Long logCreatedAt = null;

    private Long logUpdatedAt = null;

    private Integer logVersion = 0;

    public Role() 
    {

    }

    public Role(String name) 
    {
        this.name = name;
    }

    public Role(Long id) 
    {
        this.id = id;
    }

    public Role(Long id, 
            String name, 
            String status) 
    {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Role(Long id, 
            String name, 
            String status, 
            String logUser, 
            String logRole, 
            Long logCreatedAt, 
            Long logUpdatedAt) 
    {
        this.id = id;
        this.name = name;
        this.status = status;
        this.logUser = logUser;
        this.logRole = logRole;
        this.logCreatedAt = logCreatedAt;
        this.logUpdatedAt = logUpdatedAt;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Role other = (Role) obj;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }
}
