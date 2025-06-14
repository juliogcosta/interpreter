package com.yc.models.nosql;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

@Document(collection = "#{@cachePersistentContentRepositoryCustomImpl.getCollectionName()}")
public class CachePersistentContent implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 6136475217035060207L;

    @NotNull
    @Id
    private String id;

    private String content;

    private Long expires = 0L;

    @Version()
    private Integer version = null;

    public CachePersistentContent(String id, String content, Long expires, Integer version) 
    {
        this.id = id;
        this.content = content;
        this.expires = expires;

        this.version = version;
    }

    public String getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }

    public Long getExpires()
    {
        return expires;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }
}
