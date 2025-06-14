package com.yc.models.sql.up;

import java.io.Serializable;
import java.util.Objects;

public class ProjectResource implements Serializable
{
    private static final long serialVersionUID = 449643711243529142L;

    static public enum Type {
        GITHUB
    };
    
    private Long id;
    
    private Project project;
    
    private String name;

    private String type; 
    
    private String key;
    
    private String description;
    
    private String status;
    
    private String loguser;
    
    private String logrole;
    
    private Long logcreatedat;
    
    private Long logupdatedat;
    
    private Integer logversion;
    
    public ProjectResource()
    {

    }

    public ProjectResource(Long id, Project project, String name, String type, String key, String description, String status, 
            String loguser, String logrole, Long logcreatedat, Long logupdatedat, Integer logversion)
    {
        super();
        this.id = id;
        this.project = project;
        this.name = name;
        this.type = type;
        this.key = key;
        this.description = description;
        this.status = status;
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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
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

    public String getLoguser()
    {
        return loguser;
    }

    public void setLoguser(String loguser)
    {
        this.loguser = loguser;
    }

    public String getLogrole()
    {
        return logrole;
    }

    public void setLogrole(String logrole)
    {
        this.logrole = logrole;
    }

    public Long getLogcreatedat()
    {
        return logcreatedat;
    }

    public void setLogcreatedat(Long logcreatedat)
    {
        this.logcreatedat = logcreatedat;
    }

    public Long getLogupdatedat()
    {
        return logupdatedat;
    }

    public void setLogupdatedat(Long logupdatedat)
    {
        this.logupdatedat = logupdatedat;
    }

    public Integer getLogversion()
    {
        return logversion;
    }

    public void setLogversion(Integer logversion)
    {
        this.logversion = logversion;
    }

    @Override
    public String toString()
    {
        return "ProjectResource [id=" + id + ", project=" + project + ", name=" + name + ", type=" + type + ", key=" + key
                + ", description=" + description + ", status=" + status + ", loguser=" + loguser + ", logrole="
                + logrole + ", logcreatedat=" + logcreatedat + ", logupdatedat=" + logupdatedat + ", logversion="
                + logversion + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
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
        ProjectResource other = (ProjectResource) obj;
        return Objects.equals(id, other.id);
    }
}
