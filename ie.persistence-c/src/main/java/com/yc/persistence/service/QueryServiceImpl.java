package com.yc.persistence.service;

import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yc.loco3.Constant;
import com.yc.loco3.dao.DAO;
import com.yc.loco3.security.CredentialServiceImpl;
import com.yc.loco3.security.User;
import com.yc.loco3.security.UserlessAuthorizationServiceImpl;
import com.yc.loco3.transformers.queries.Helper;
import com.yc.loco3.transformers.queries.ReadStatementTransformation;
import com.yc.loco3.transformers.queries.rmap.SQLRowMapper;
import com.yc.persistence.api.client.DBRDriverRemoteImpl;
import com.yc.persistence.exception.InterpreterControlledException;
import com.yc.persistence.parser.QueryParser;

@Service
public class QueryServiceImpl implements DAO
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DBRDriverRemoteImpl dbrDriverRemote;

    /*@Autowired
    private LocalCacheImpl schemaCache;

    @Autowired
    private SQLStatementCache sqlStatementCacheWrapper;*/
    private SchemaCacheServiceImpl schemaCacheServiceImpl;

    public JSONArray readByCriteria(String traceId, String spanId, String parentSpanId, String tenantId, JSONArray datas, User user) throws InterpreterControlledException, Exception
    {
        JSONArray response = new JSONArray();

        for (int dataIdx = 0; dataIdx < datas.length(); dataIdx++)
        {
            JSONObject data = datas.getJSONObject(dataIdx);

            if (data.has(Constant._cache))
            {
                data.remove(Constant._cache).toString();
            }

            String sPaging = null;
            if (data.has(Constant._paging))
            {
                sPaging = data.remove(Constant._paging).toString();
            }
            else
            {
                JSONObject paging = new JSONObject();
                paging.put(Constant._maxRegisters, 1000);
                paging.put(Constant._firstRegister, 0);

                sPaging = paging.toString();
            }

            String sSorting = null;
            if (data.has(Constant._sorting))
            {
                sSorting = data.remove(Constant._sorting).toString();
            }
            else
            {
                JSONObject sorting = new JSONObject();
                sorting.put(Constant._orderBy, Constant.id);
                sorting.put(Constant._order, "ASC");

                sSorting = sorting.toString();
            }

            String sConnective = "AND";
            if (data.has(Constant._connective))
            {
                sConnective = data.remove(Constant._connective).toString();
            }

            Boolean sCount = false;
            if (data.has(Constant._count))
            {
                sCount = Boolean.parseBoolean(data.remove(Constant._count).toString());
            }

            String entityName = data.keys().next();

            JSONArray queries = new QueryParser().parse(data);

            for (int idx = 0; idx < queries.length(); idx++)
            {
                queries.getJSONObject(idx).put(Constant._paging, new JSONObject(sPaging));
                queries.getJSONObject(idx).put(Constant._sorting, new JSONObject(sSorting));
                queries.getJSONObject(idx).put(Constant._connective, sConnective);
                queries.getJSONObject(idx).put(Constant._count, sCount);

                try
                {
                    JSONObject content = null;
                    if (user == null)
                    {
                        content = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), null);
                    }
                    else
                    {
                        content = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), user);
                    }

                    response.put(content);
                }
                catch (Exception e)
                {
                    throw e;
                }
            }
        }

        return response;
    }

    @Override
    public JSONObject readById(String traceId, String spanId, String parentSpanId, String tenantId, JSONObject jsObject) throws InterpreterControlledException, Exception
    {
        JSONArray jsArray = new JSONArray();
        jsArray.put(jsObject);
        jsArray = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, jsArray, null);
        if (jsArray.length() == 0)
        {
            throw new Exception("204:object not found.");
        }
        else
        {
            JSONObject jsO = jsArray.getJSONObject(0);
            if (jsO.keys().hasNext())
            {
                if (jsO.getJSONArray(jsO.keys().next()).length() == 0)
                {
                    throw new Exception("204:object not found.");
                }
                else 
                {
                    return jsO.getJSONArray(jsO.keys().next()).getJSONObject(0);
                }
            }
            else
            {
                throw new Exception("204:object not found.");
            }
        }
    }

    private JSONObject readByCriteria(String traceId, String spanId, String parentSpanId, String tenantId, String entityName, Boolean count, JSONObject data, User user) throws InterpreterControlledException, Exception
    {
        Helper helper = null;

        SchemaCacheWrapper schemaCacheWrapper = new SchemaCacheWrapper(this.schemaCacheServiceImpl, traceId, spanId, parentSpanId, tenantId);

        if (user == null)
        {
            helper = new ReadStatementTransformation(schemaCacheWrapper)
                    .getReadRelationalStatement(tenantId, entityName, data, new UserlessAuthorizationServiceImpl(), new HashSet<>());
        }
        else
        {
            helper = new ReadStatementTransformation(schemaCacheWrapper)
                    .getReadRelationalStatement(tenantId, entityName, data, new CredentialServiceImpl(user), new HashSet<>());
        }

        if (helper.simpleQuery)
        {
            JSONObject object = new JSONObject();
            object.put(entityName, this.dbrDriverRemote.selectAsJSON(traceId, spanId, parentSpanId, tenantId, helper.query));

            return object;
        }
        else
        {
            JSONArray datas = this.dbrDriverRemote.select(traceId, spanId, parentSpanId, tenantId, helper.query);

            return new SQLRowMapper(schemaCacheWrapper).getDatas(tenantId, entityName, data, datas, count);
        }
    }
}