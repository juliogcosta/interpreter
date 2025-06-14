package com.yc.persistence.service;

import org.json.JSONObject;

import br.edu.ufrn.loco3.schema.SchemaService;

class SchemaCacheWrapper implements SchemaService
{
    private String tenantId = null;
    private JSONObject cache = null;

    private SchemaCacheServiceImpl schemaCacheService;

    public SchemaCacheWrapper(SchemaCacheServiceImpl schemaCacheService, String traceId, String spanId, String parentSpanId, String tenantId) throws Exception 
    {
        this.schemaCacheService = schemaCacheService;
        this.tenantId = tenantId;

        final JSONObject register = this.schemaCacheService.read(traceId, spanId, parentSpanId, "persistence-service-".concat(tenantId));
        if (register.getBoolean("status"))
        {
            this.cache = register.getJSONObject("content");
        }
    }

    public String getTenantId()
    {
        return this.tenantId;
    }

    @Override
    public String getSchemaName(String tenantId)
    {
        return this.cache.getJSONObject("schema").getString("name");
    }

    @Override
    public JSONObject getOOStatements(String tenantId)
    {
        return this.cache.getJSONObject("schema").getJSONObject("entities");
    }

    @Override
    public String getOrgName(String tenantId)
    {

        return null;
    }
}