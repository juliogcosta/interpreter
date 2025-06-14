package com.yc.models.sql.up;

import java.io.Serializable;
import java.util.Objects;

public class Project implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    static public enum State {
        INSTALLING, MODELING, RUNNING, SUSPENDED
    };
    
    static public enum EnvType {
        DEVELOPMENT, PRODUCTION
    };
    
    public interface DriverClassName
    {
        final static String PostgreSQL = "org.postgresql.Driver";
    }
    
    public interface ProtocolJDBCUrl
    {
        final static String PostgreSQL = "jdbc:postgresql://";
    }
    
    public interface XADatasourceClassname
    {
        final static String PostgreSQL = "org.postgresql.xa.PGXADataSource";
    }
    
    private Long id;
    
    private String name;
    
    private String owner;
    
    private String alias;
    
    private String email;
    
    private String description;
    
    private String envType;
    
    private String status;
    
    private String secret;
    
    private String loguser;
    
    private String logrole;
    
    private Long logcreatedat;
    
    private Long logupdatedat;
    
    private Integer logversion;
    
    public Project() {
        
    }

    public Project(Long id) {
        this.id = id;
    }
    
    public Project(Long id, 
        String name, 
        String owner, 
        String alias, 
        String email,
        String description, 
        String status, 
        String envType, 
        String secret, 
        String loguser,
        String logrole,
        Long logcreatedat,
        Long logupdatedat,
        Integer logversion) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.alias = alias;
        this.email = email;
        this.description = description;
        this.status = status;
        this.envType = envType;
        this.secret = secret;
        this.loguser = loguser;
        this.logrole = logrole;
        this.logcreatedat = logcreatedat;
        this.logupdatedat = logupdatedat;
        this.logversion = logversion;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getEnvType()
    {
        return envType;
    }

    public void setEnvType(String envType)
    {
        this.envType = envType;
    }

    public String getSecret()
    {
        return secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
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
        return "Project [id=" + id + ", name=" + name + ", owner=" + owner + ", alias=" + alias
            + ", email=" + email + ", description=" + description
            + ", status=" + status + ", envType=" + envType + ", loguser=" + loguser + ", logrole=" + logrole + ", logcreatedat=" + logcreatedat
            + ", logupdatedat=" + logupdatedat + ", logversion=" + logversion + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, owner);
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
        Project other = (Project) obj;
        return Objects.equals(name, other.name) && Objects.equals(owner, other.owner);
    }
}
