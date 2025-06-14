package com.yc.models.nosql;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document
public class MaestroParticipantNotification 
{
    @Id
    private String id = null;
    
    private String org = null;

    private String participant = null;
    
    private String key = null;
    
    private String value = null;
    
    private Date date = null;
    
    private String status = null;
    
    private String loguser = null;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getOrg()
    {
        return org;
    }

    public void setOrg(String org)
    {
        this.org = org;
    }

    public String getParticipant()
    {
        return participant;
    }

    public void setParticipant(String participant)
    {
        this.participant = participant;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
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
        return status;
    }

    public void setLoguser(String loguser)
    {
        this.loguser = loguser;
    }

    @Override
    public String toString()
    {
        return "MaestroParticipantNotification [id=" + id + ", org=" + org + ", participant=" + participant + ", key="
                + key + ", value=" + value + ", date=" + date + ", status=" + status + ", loguser=" + loguser + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(key);
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
        MaestroParticipantNotification other = (MaestroParticipantNotification) obj;
        return Objects.equals(key, other.key);
    }
}
