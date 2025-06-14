package com.yc.utils.bean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class LocalSQLStatementCacheImpl
{
    private static final Map<String, String> SQL_STATEMENT_CACHE = new HashMap<>();

    public void save(String tenantId, String queryId, String queryType, String query)
    {
        SQL_STATEMENT_CACHE.put(tenantId.concat(".").concat(queryId).concat(".").concat(queryType), query);;
    }

    public String get(String tenantId, String queryId, String queryType)
    {
        if (this.exist(tenantId, queryId, queryType))
        {
            return SQL_STATEMENT_CACHE.get(tenantId.concat(".").concat(queryId).concat(".").concat(queryType));
        }

        return null;
    }

    public Boolean exist(String tenantId, String queryId, String queryType)
    {
        return SQL_STATEMENT_CACHE.containsKey(tenantId.concat(".").concat(queryId).concat(".").concat(queryType));
    }

    public void remove(String tenantId, String queryId, String queryType)
    {
        SQL_STATEMENT_CACHE.remove(tenantId.concat(".").concat(queryId).concat(".").concat(queryType));
    }
}