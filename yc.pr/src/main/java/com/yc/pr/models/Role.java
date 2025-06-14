package com.yc.pr.models;

import java.io.Serializable;

public class Role implements Serializable
{
    /**
     * 
     */
    final static private long serialVersionUID = -2505037989836471597L;

    private Long id = -1L;

    private String name = null;

    private String owner = null;

    private String status = null;

    public Role()
    {

    }

    public Role(String name)
    {
        this.name = name;
    }

    public Role(String name, String status)
    {
        this.name = name;

        this.status = status;
    }

    public Role(Long id, String name, String status)
    {
        this.id = id;

        this.name = name;

        this.status = status;
    }

    public Role(Long id, String name, String owner, String status)
    {
        this.id = id;

        this.name = name;

        this.owner = owner;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
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

    @Override
    public String toString()
    {
        return "Role [id=" + id + ", name=" + name + ", status=" + status + "]";
    }
}
