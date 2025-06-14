package com.yc.models.sql.up;

import java.io.Serializable;
import java.util.Objects;

public class DataSchema implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static public enum State {
        INSTALLING, MODELING, RUNNING, SUSPENDED
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
    
    private Project project;

    private String name;
    
    private String tenantid;

    private String alias;

    private String description;

    private String status;

    private String dbsqlname;

    private String dbsqlusername;

    private String dbsqluserpassword;

    private String dbsqlversion;

    private String dbsqldriverclassname;

    private Integer dbsqlminimumconnidle;

    private Integer dbsqlmaximumpoolsize;

    private String dbsqlxadatasourceclassname;

    private String dbsqljdbcurl;

    private String dbsqlserveraddress;

    private String dbsqlservername;

    private Integer dbsqlserverport;

    private String dbnosqlname;

    private String dbnosqlcluster;

    private String dbnosqlusername;

    private String dbnosqluserpassword;

    private String dbnosqlversion;

    private String dbnosqlserveraddress;

    private String dbnosqlservername;

    private Integer dbnosqlserverport;

    private String loguser;

    private String logrole;

    private Long logcreatedat;

    private Long logupdatedat;

    private Integer logversion;

    public DataSchema() 
    {
        this.dbsqldriverclassname = DriverClassName.PostgreSQL;

        this.dbsqlxadatasourceclassname = XADatasourceClassname.PostgreSQL;
        
        this.dbsqljdbcurl = ProtocolJDBCUrl.PostgreSQL;
    }

    public DataSchema(Long id, 
            Project project, 
            String name, 
            String alias, 
            String description, 
            String status, 
            String tenantId, 
            String dbSqlName,
            String dbSqlUserName, 
            String dbSqlUserPassword, 
            String dbSqlVersion, 
            String dbSqlDriverClassName, 
            Integer dbSqlMinimumConnIdle, 
            Integer dbSqlMaximumPoolSize, 
            String dbSqlXaDatasourceClassname, 
            String dbSqlJdbcUrl, 
            String dbSqlServerAddress, 
            String dbSqlServerName, 
            Integer dbSqlServerPort, 
            String dbNoSqlName,
            String dbNoSqlUserName, 
            String dbNoSqlUserPassword, 
            String dbNoSqlCluster, 
            String dbNoSqlVersion, 
            String dbNoSqlServerAddress, 
            String dbNoSqlServerName, 
            Integer dbNoSqlServerPort, 
            String logUser, 
            String logRole, 
            Long logCreatedAt, 
            Long logUpdatedAt, 
            Integer logVersion)
    {
        this.id = id;
        this.project = project;
        this.name = name;
        this.alias = alias;
        this.description = description;
        this.status = status;
        this.tenantid = tenantId;
        this.dbsqlname = dbSqlName;
        this.dbsqlusername = dbSqlUserName;
        this.dbsqluserpassword = dbSqlUserPassword;
        this.dbsqlversion = dbSqlVersion;
        this.dbsqldriverclassname = dbSqlDriverClassName;
        this.dbsqlminimumconnidle = dbSqlMinimumConnIdle;
        this.dbsqlmaximumpoolsize = dbSqlMaximumPoolSize;
        this.dbsqlxadatasourceclassname = dbSqlXaDatasourceClassname;
        this.dbsqljdbcurl = dbSqlJdbcUrl;
        this.dbsqlserveraddress = dbSqlServerAddress;
        this.dbsqlservername = dbSqlServerName;
        this.dbsqlserverport = dbSqlServerPort;
        this.dbnosqlname = dbNoSqlName;
        this.dbnosqlusername = dbNoSqlUserName;
        this.dbnosqluserpassword = dbNoSqlUserPassword;
        this.dbnosqlversion = dbNoSqlVersion;
        this.dbnosqlcluster = dbNoSqlCluster;
        this.dbnosqlserveraddress = dbNoSqlServerAddress;
        this.dbnosqlservername = dbNoSqlServerName;
        this.dbnosqlserverport = dbNoSqlServerPort;
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

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTenantid()
    {
        return tenantid;
    }

    public void setTenantid(String tenantId)
    {
        this.tenantid = tenantId;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
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

    public String getDbsqldriverclassname()
    {
        return dbsqldriverclassname;
    }

    public void setDbsqldriverclassname(String dbSqlDriverClassName)
    {
        this.dbsqldriverclassname = dbSqlDriverClassName;
    }

    public Integer getDbsqlminimumconnidle()
    {
        return dbsqlminimumconnidle;
    }

    public void setDbsqlminimumconnidle(Integer dbSqlMinimumConnIdle)
    {
        this.dbsqlminimumconnidle = dbSqlMinimumConnIdle;
    }

    public Integer getDbsqlmaximumpoolsize()
    {
        return dbsqlmaximumpoolsize;
    }

    public void setDbsqlmaximumpoolsize(Integer dbSqlMaximumPoolSize)
    {
        this.dbsqlmaximumpoolsize = dbSqlMaximumPoolSize;
    }

    public String getDbsqlxadatasourceclassname()
    {
        return dbsqlxadatasourceclassname;
    }

    public void setDbsqlxadatasourceclassname(String dbSqlXaDatasourceClassname)
    {
        this.dbsqlxadatasourceclassname = dbSqlXaDatasourceClassname;
    }

    public String getDbsqljdbcurl()
    {
        return dbsqljdbcurl;
    }

    public void setDbsqljdbcurl(String dbSqlJdbcUrl)
    {
        this.dbsqljdbcurl = dbSqlJdbcUrl;
    }

    public String getDbsqlserveraddress()
    {
        return dbsqlserveraddress;
    }

    public void setDbsqlserveraddress(String dbSqlServerAddress)
    {
        this.dbsqlserveraddress = dbSqlServerAddress;
    }

    public String getDbsqlservername()
    {
        return dbsqlservername;
    }

    public void setDbsqlservername(String dbSqlServerName)
    {
        this.dbsqlservername = dbSqlServerName;
    }

    public Integer getDbsqlserverport()
    {
        return dbsqlserverport;
    }

    public void setDbsqlserverport(Integer dbSqlServerPort)
    {
        this.dbsqlserverport = dbSqlServerPort;
    }

    public String getDbnosqlname()
    {
        return dbnosqlname;
    }

    public void setDbnosqlname(String dbNoSqlName)
    {
        this.dbnosqlname = dbNoSqlName;
    }

    public String getDbnosqlcluster()
    {
        return dbnosqlcluster;
    }

    public void setDbnosqlcluster(String dbNoSqlCluster)
    {
        this.dbnosqlcluster = dbNoSqlCluster;
    }

    public String getDbnosqlusername()
    {
        return dbnosqlusername;
    }

    public void setDbnosqlusername(String dbNoSqlUserName)
    {
        this.dbnosqlusername = dbNoSqlUserName;
    }

    public String getDbnosqluserpassword()
    {
        return dbnosqluserpassword;
    }

    public void setDbnosqluserpassword(String dbNoSqlUserPassword)
    {
        this.dbnosqluserpassword = dbNoSqlUserPassword;
    }

    public String getDbnosqlversion()
    {
        return dbnosqlversion;
    }

    public void setDbnosqlversion(String dbNoSqlVersion)
    {
        this.dbnosqlversion = dbNoSqlVersion;
    }

    public String getDbnosqlserveraddress()
    {
        return dbnosqlserveraddress;
    }

    public void setDbnosqlserveraddress(String dbNoSqlServerAddress)
    {
        this.dbnosqlserveraddress = dbNoSqlServerAddress;
    }

    public String getDbnosqlservername()
    {
        return dbnosqlservername;
    }

    public void setDbnosqlservername(String dbNoSqlServerName)
    {
        this.dbnosqlservername = dbNoSqlServerName;
    }

    public Integer getDbnosqlserverport()
    {
        return dbnosqlserverport;
    }

    public void setDbnosqlserverport(Integer dbNoSqlServerPort)
    {
        this.dbnosqlserverport = dbNoSqlServerPort;
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
    public int hashCode()
    {
        return Objects.hash(name, project);
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
        DataSchema other = (DataSchema) obj;
        return Objects.equals(name, other.name) && Objects.equals(project, other.project);
    }

    @Override
    public String toString()
    {
        return "DataSchema [id=" + id + ", project=" + project + ", name=" + name + ", tenantid=" + tenantid
                + ", alias=" + alias + ", description=" + description + ", status="
                + status + ", dbsqlname=" + dbsqlname + ", dbsqlusername=" + dbsqlusername + ", dbsqluserpassword="
                + dbsqluserpassword + ", dbsqlversion=" + dbsqlversion + ", dbsqldriverclassname="
                + dbsqldriverclassname + ", dbsqlminimumconnidle=" + dbsqlminimumconnidle + ", dbsqlmaximumpoolsize="
                + dbsqlmaximumpoolsize + ", dbsqlxadatasourceclassname=" + dbsqlxadatasourceclassname
                + ", dbsqljdbcurl=" + dbsqljdbcurl + ", dbsqlserveraddress=" + dbsqlserveraddress + ", dbsqlservername="
                + dbsqlservername + ", dbsqlserverport=" + dbsqlserverport + ", dbnosqlname=" + dbnosqlname
                + ", dbnosqlcluster=" + dbnosqlcluster + ", dbnosqlusername=" + dbnosqlusername
                + ", dbnosqluserpassword=" + dbnosqluserpassword + ", dbnosqlversion=" + dbnosqlversion
                + ", dbnosqlserveraddress=" + dbnosqlserveraddress + ", dbnosqlservername=" + dbnosqlservername
                + ", dbnosqlserverport=" + dbnosqlserverport + ", loguser=" + loguser + ", logrole=" + logrole
                + ", logcreatedat=" + logcreatedat + ", logupdatedat=" + logupdatedat + ", logversion=" + logversion
                + "]";
    }
}