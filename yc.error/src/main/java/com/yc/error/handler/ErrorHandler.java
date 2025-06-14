package com.yc.error.handler;

import org.json.JSONObject;

public interface ErrorHandler
{
    JSONObject buildResponseEntityError(
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
            String actionUuid, 
            String body, 
            Boolean saveTrace, 
            Boolean saveBody);

    JSONObject getError(Exception e);
}
