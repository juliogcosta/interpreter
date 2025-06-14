package com.yc.models.nosql;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Document(collection = "#{@consumptionRepositoryCustomImpl.getCollectionName()}")
public class DataTrafficConsumption implements Serializable
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
    private String tenantId = null;
    
    private Long accumulated = null;
    
    public String getTenantId()
    {
        return tenantId;
    }
    
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }
    
    public Long getAccumulated()
    {
        return accumulated;
    }
    
    public void setAccumulated(Long accumulatedAmount)
    {
        this.accumulated = accumulatedAmount;
    }
    
    @Override
    public String toString()
    {
        return "Consumption [tenantId=" + tenantId + ", accumulated="
            + accumulated + "]";
    }
}
