package com.yc.models.nosql;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@errorRepositoryCustomImpl.getCollectionName()}")
public class Error extends Register
{
    private String trace;
    
    public String getTrace()
    {
        return trace;
    }
    
    public void setTrace(String trace)
    {
        this.trace = trace;
    }

    @Override
    public String toString()
    {
        return "Error [trace=" + trace + ", getId()=" + getId() + ", getHttpStatusCode()=" + getHttpStatusCode()
                + ", getTenantId()=" + getTenantId() + ", getUsername()=" + getUsername() + ", getOrgName()="
                + getOrgName() + ", getProjectName()=" + getProjectName() + ", getTraceId()=" + getTraceId()
                + ", getSpanId()=" + getSpanId() + ", getParentSpanId()=" + getParentSpanId() + ", getHttpMethod()="
                + getHttpMethod() + ", getHttpEndpoint()=" + getHttpEndpoint() + ", getMessage()=" + getMessage()
                + ", getCustomMessage()=" + getCustomMessage() + ", getBody()=" + getBody() + ", getDatetime()="
                + getDatetime() + ", getTimestamp()=" + getTimestamp() + ", getService()=" + getService()
                + ", getActionUuid()=" + getActionUuid() + ", getClass()=" + getClass()
                + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
}
