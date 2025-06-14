package com.yc.loco3.schema;

public interface SQLStatementService
{
    public String getSQLStatement(String tenantId, String queryId, String queryType);
}
