package com.yc.models.sql.up;

import java.io.Serializable;

public class Emailservice implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String owner;
    
    private String name;

    private String token;

    private String status;

    private String loguser;

    private String logrole;

    private Long logcreatedat;

    private Long logupdatedat;

    private Integer logversion;

    public Emailservice() 
    {
    }

    public Emailservice(Long id, 
            String owner,  
            String name,
            String token, 
            String status, 
            String logUser, 
            String logRole, 
            Long logCreatedAt, 
            Long logUpdatedAt, 
            Integer logVersion)
    {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.token = token;
        this.status = status;
        this.logversion = logVersion;
        this.loguser = logUser;
        this.logrole = logRole;
        this.logcreatedat = logCreatedAt;
        this.logupdatedat = logUpdatedAt;
        this.logversion = logVersion;
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

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getLoguser()
    {
        return loguser;
    }

    public void setLoguser(String logUser)
    {
        this.loguser = logUser;
    }

    public String getLogrole()
    {
        return logrole;
    }

    public void setLogrole(String logRole)
    {
        this.logrole = logRole;
    }

    public Long getLogcreatedat()
    {
        return logcreatedat;
    }

    public void setLogcreatedat(Long logCreatedAt)
    {
        this.logcreatedat = logCreatedAt;
    }

    public Long getLogupdatedat()
    {
        return logupdatedat;
    }

    public void setLogupdatedat(Long logUpdatedAt)
    {
        this.logupdatedat = logUpdatedAt;
    }

    public Integer getLogversion()
    {
        return logversion;
    }

    public void setLogversion(Integer logVersion)
    {
        this.logversion = logVersion;
    }

    @Override
    public String toString()
    {
        return "Emailservice [id=" + id + ", owner=" + owner + ", name=" + name + ", token=" + token + ", status=" + status + ", loguser="
                + loguser + ", logrole=" + logrole + ", logcreatedat=" + logcreatedat + ", logupdatedat=" + logupdatedat
                + ", logversion=" + logversion + "]";
    }
}