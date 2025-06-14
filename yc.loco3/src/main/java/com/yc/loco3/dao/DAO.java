package com.yc.loco3.dao;

import org.json.JSONObject;

public interface DAO
{
    public JSONObject readById(String traceId, String spanId, String parentSpanId, String tenantId, JSONObject jsObject) throws Exception;
}
