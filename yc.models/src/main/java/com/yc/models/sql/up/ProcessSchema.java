package com.yc.models.sql.up;

import java.io.Serializable;
import java.util.Objects;

public class ProcessSchema implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static public enum State {
        INSTALLING, MODELING, RUNNING, SUSPENDED
    };

    private Long id;

    private Project project;

    private String name;

    private String alias;

    private String description;

    private String status;

    private String processes;

    private String loguser;

    private String logrole;

    private Long logcreatedat; 

    private Long logupdatedat;

    private Integer logversion;

    public ProcessSchema() {
    }

    public ProcessSchema(Long id, 
            Project project, 
            String name, 
            String alias, 
            String description, 
            String status,
            String processes, 
            String logUser, 
            String logRole, 
            Long logCreatedAt, 
            Long logUpdatedAt, 
            Integer logVersion) {
        this.id = id;
        this.project = project;
        this.name = name;
        this.alias = alias;
        this.description = description;
        this.status = status;
        this.processes = processes;
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

    public String getProcesses()
    {
        return processes;
    }

    public void setProcesses(String processes)
    {
        this.processes = processes;
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
        ProcessSchema other = (ProcessSchema) obj;
        return Objects.equals(name, other.name) && Objects.equals(project, other.project);
    }
}
