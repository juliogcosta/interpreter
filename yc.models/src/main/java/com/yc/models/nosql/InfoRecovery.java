package com.yc.models.nosql;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document(collection = "info_recovery")
public class InfoRecovery implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    static public enum State {
        ACTIVE, INACTIVE
    };
    
    @Id
    private String id;
    
    private Integer status;
    
    private String username;
    
    private String type;
    
    private String info;
    
    private Long expiresin;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getInfo()
    {
        return info;
    }
    
    public void setInfo(String info)
    {
        this.info = info;
    }
    
    public Long getExpiresin()
    {
        return expiresin;
    }
    
    public void setExpiresin(Long createdat)
    {
        this.expiresin = createdat;
    }
    
    @Override
    public String toString()
    {
        return "InfoRecovery [id=" + id + ", status=" + status + ", username="
            + username + ", type=" + type + ", info=" + info + ", expiresin="
            + expiresin + "]";
    }
}
