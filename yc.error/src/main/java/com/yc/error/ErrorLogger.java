package com.yc.error;

import org.json.JSONObject;

public interface ErrorLogger
{
    public JSONObject buildErrorMessage(
            String contextManagerControlKey, 
            Exception e, 
            String tenantId, 
            String traceId, 
            String spanId, 
            String parentSpanId, 
            String orgName, 
            String projectName, 
            String username, 
            String service, 
            String httpEndpoint, 
            String httpMethod, 
            String elementUuid, 
            String httpBody, 
            String queueTarget,
            Boolean full);
}
