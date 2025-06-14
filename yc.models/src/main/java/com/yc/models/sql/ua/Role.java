package com.yc.models.sql.ua;

import java.io.Serializable;
import java.util.Objects;

public class Role extends com.yc.models.sql.up.Role implements Serializable
{
    private static final long serialVersionUID = 8523789719437617302L;

    private String owner = null;

    private String name = null;

    private String label = null;

    private Boolean ispublic = false;

    public Role()
    {

    }

    public Role(Long id, 
            String name,
            String owner, 
            String status,
            Boolean ispublic) 
    {
        super(id, name, status);

        this.name = name;
        this.owner = owner;
        this.ispublic = ispublic;
    }

    public Role(String name,
            String owner, 
            String label, 
            String status,
            Boolean ispublic) 
    {
        super(null, name, status);

        this.name = name;
        this.owner = owner;
        this.label = label;
        this.ispublic = ispublic;
    }

    public Role(Long id, 
            String name, 
            String owner, 
            String label, 
            String status,
            Boolean ispublic, 
            String logUser, 
            String logRole, 
            Long logCreatedAt, 
            Long logUpdatedAt, 
            Integer logVersion) 
    {
        super(id, 
                name, 
                status, 
                logUser, 
                logRole, 
                logCreatedAt, 
                logUpdatedAt);

        this.name = name;
        this.owner = owner;
        this.label = label;
        this.ispublic = ispublic;
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

    public void setName(String ownerName)
    {
        this.name = ownerName;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public Boolean getIspublic()
    {
        return ispublic;
    }
    
    public void setIspublic(Boolean ispublic)
    {
        this.ispublic = ispublic;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(name, owner);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        return Objects.equals(name, other.name) && Objects.equals(owner, other.owner);
    }

    @Override
    public String toString()
    {
        return "Role [getId()=" + getId() + ", getName()=" + getName()+ ", owner=" + owner + ", label=" + label + ", ispublic=" + ispublic
                + ", getStatus()=" + getStatus() + "]";
    }
}
