package com.yc.loco3.schema;

import org.json.JSONObject;

public interface SchemaService
{
    public String getSchemaName(String tenantId);

    public JSONObject getOOStatements(String tenantId);

    public String getOrgName(String tenantId);
}
