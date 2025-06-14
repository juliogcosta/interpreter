package com.yc.pr.models;

public class Org
{
    private String name;
    
    private String owner;
    
    private String alias;
    
    private String status;
    
    public Org() {

    }

    public Org(String name, String owner, String status) {
        this.name = name;
        this.owner = owner;
        this.status = status;
    }

    public Org(String name, String owner, String alias, String status) {
        this.name = name;
        this.owner = owner;
        this.alias = alias;
        this.status = status;
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

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
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
        return "Org [name=" + name + ", owner=" + owner + ", alias=" + alias + ", status=" + status + "]";
    }
}
