package com.yc.models.nosql;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Document(collection = "#{@consumptionRepositoryCustomImpl.getCollectionName()}")
public class Consumption implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 6136475217035060207L;
    
    static public enum Type {
        Request, Response, Complement
    };
    
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    
    private String tenantId = null;
    
    private String endpointUrl = null;
    
    private Integer type = null;
    
    private Integer length = null;
    
    private Integer action = null;
    
    private Integer latency = null;
    
    private Long datetime = null;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getTenantId()
    {
        return tenantId;
    }
    
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }
    
    public String getEndpointUrl()
    {
        return endpointUrl;
    }
    
    public void setEndpointUrl(String endpointUrl)
    {
        this.endpointUrl = endpointUrl;
    }
    
    public Long getDatetime()
    {
        return datetime;
    }
    
    public void setDatetime(Long datetime)
    {
        this.datetime = datetime;
    }
    
    public Integer getType()
    {
        return type;
    }
    
    public void setType(Integer type)
    {
        this.type = type;
    }
    
    public Integer getLength()
    {
        return length;
    }
    
    public void setLength(Integer length)
    {
        this.length = length;
    }
    
    public Integer getAction()
    {
        return action;
    }
    
    public void setAction(Integer action)
    {
        this.action = action;
    }
    
    public Integer getLatency()
    {
        return latency;
    }
    
    public void setLatency(Integer latency)
    {
        this.latency = latency;
    }
    
    @Override
    public String toString()
    {
        return "Consumption [id=" + id + ", tenantId=" + tenantId + ", action="
            + action + ", type=" + type + ", latency=" + latency
            + ", endpointUrl=" + endpointUrl + ", length=" + length
            + ", datetime=" + datetime + "]";
    }
}
