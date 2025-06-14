package com.yc.models.nosql;

import jakarta.persistence.Id;

public abstract class Register
{
    @Id
    private String id = null;
    
    private Integer httpStatusCode = null;
    
    private String tenantId = null;
    
    private String username = null;
    
    private String orgName = null;
    
    private String projectName = null;
    
    private String traceId = null;
    
    private String spanId = null;
    
    private String parentSpanId = null;
    
    private String httpMethod = null;
    
    private String httpEndpoint = null;
    
    private String actionUuid = null;
    
    private String message = null;
    
    private String customMessage = null;
    
    private String body = null;
    
    private Long datetime = null;
    
    private String timestamp = null;
    
    private String service = null;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public Integer getHttpStatusCode()
    {
        return httpStatusCode;
    }
    
    public void setHttpStatusCode(Integer httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }
    
    public String getTenantId()
    {
        return tenantId;
    }
    
    public void setTenantId(String tenantId)
    {
        this.tenantId = tenantId;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getOrgName()
    {
        return orgName;
    }
    
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }
    
    public String getProjectName()
    {
        return projectName;
    }
    
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }
    
    public String getTraceId()
    {
        return traceId;
    }
    
    public void setTraceId(String traceId)
    {
        this.traceId = traceId;
    }
    
    public String getSpanId()
    {
        return spanId;
    }
    
    public void setSpanId(String spanId)
    {
        this.spanId = spanId;
    }
    
    public String getParentSpanId()
    {
        return parentSpanId;
    }
    
    public void setParentSpanId(String parentSpanId)
    {
        this.parentSpanId = parentSpanId;
    }
    
    public String getHttpMethod()
    {
        return httpMethod;
    }
    
    public void setHttpMethod(String httpMethod)
    {
        this.httpMethod = httpMethod;
    }
    
    public String getHttpEndpoint()
    {
        return httpEndpoint;
    }
    
    public void setHttpEndpoint(String httpEndpoint)
    {
        this.httpEndpoint = httpEndpoint;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getCustomMessage()
    {
        return customMessage;
    }
    
    public void setCustomMessage(String customMressage)
    {
        this.customMessage = customMressage;
    }
    
    public String getBody()
    {
        return body;
    }
    
    public void setBody(String body)
    {
        this.body = body;
    }
    
    public Long getDatetime()
    {
        return datetime;
    }
    
    public void setDatetime(Long datetime)
    {
        this.datetime = datetime;
    }
    
    public String getTimestamp()
    {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
    
    public String getService()
    {
        return service;
    }
    
    public void setService(String service)
    {
        this.service = service;
    }
    
    public String getActionUuid()
    {
        return actionUuid;
    }
    
    public void setActionUuid(String actionUuid)
    {
        this.actionUuid = actionUuid;
    }
}
