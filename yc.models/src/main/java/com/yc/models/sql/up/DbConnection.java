package com.yc.models.sql.up;

import java.io.Serializable;

public class DbConnection implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    
    private String owner;

    private String dbsqlname;

    private String dbsqlusername;

    private String dbsqluserpassword;

    private String dbsqlversion;

    private String dbsqlserveraddress;

    private Integer dbsqlserverport;

    private String loguser;

    private String logrole;

    private Long logcreatedat;

    private Long logupdatedat;

    private Integer logversion;

    public DbConnection() 
    {
    }

    public DbConnection(Long id, 
            String owner,  
            String dbSqlName,
            String dbSqlUserName, 
            String dbSqlUserPassword, 
            String dbSqlVersion, 
            String dbSqlServerAddress, 
            Integer dbSqlServerPort, 
            String logUser, 
            String logRole, 
            Long logCreatedAt, 
            Long logUpdatedAt, 
            Integer logVersion)
    {
        this.id = id;
        this.owner = owner;
        this.dbsqlname = dbSqlName;
        this.dbsqlusername = dbSqlUserName;
        this.dbsqluserpassword = dbSqlUserPassword;
        this.dbsqlversion = dbSqlVersion;
        this.dbsqlserveraddress = dbSqlServerAddress;
        this.dbsqlserverport = dbSqlServerPort;
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

    public String getDbsqlname()
    {
        return dbsqlname;
    }

    public void setDbsqlname(String dbSqlName)
    {
        this.dbsqlname = dbSqlName;
    }

    public String getDbsqlusername()
    {
        return dbsqlusername;
    }

    public void setDbsqlusername(String dbSqlUserName)
    {
        this.dbsqlusername = dbSqlUserName;
    }

    public String getDbsqluserpassword()
    {
        return dbsqluserpassword;
    }

    public void setDbsqluserpassword(String dbSqlUserPassword)
    {
        this.dbsqluserpassword = dbSqlUserPassword;
    }

    public String getDbsqlversion()
    {
        return dbsqlversion;
    }

    public void setDbsqlversion(String dbSqlVersion)
    {
        this.dbsqlversion = dbSqlVersion;
    }

    public String getDbsqlserveraddress()
    {
        return dbsqlserveraddress;
    }

    public void setDbsqlserveraddress(String dbSqlServerAddress)
    {
        this.dbsqlserveraddress = dbSqlServerAddress;
    }

    public Integer getDbsqlserverport()
    {
        return dbsqlserverport;
    }

    public void setDbsqlserverport(Integer dbSqlServerPort)
    {
        this.dbsqlserverport = dbSqlServerPort;
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
        return "DbConnection [id=" + id + ", owner=" + owner + ", dbsqlname=" + dbsqlname + ", dbsqlusername="
                + dbsqlusername + ", dbsqluserpassword=" + dbsqluserpassword + ", dbsqlversion=" + dbsqlversion
                + ", dbsqlserveraddress=" + dbsqlserveraddress + ", dbsqlserverport=" + dbsqlserverport + ", loguser="
                + loguser + ", logrole=" + logrole + ", logcreatedat=" + logcreatedat + ", logupdatedat=" + logupdatedat
                + ", logversion=" + logversion + "]";
    }

}