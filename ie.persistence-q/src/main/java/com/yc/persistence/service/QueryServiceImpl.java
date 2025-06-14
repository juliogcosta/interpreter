package com.yc.persistence.service;

import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yc.persistence.api.client.CacheRemoteImpl;
import com.yc.persistence.api.client.DBNDriverRemoteImpl;
import com.yc.persistence.api.client.DBRDriverRemoteImpl;
import com.yc.persistence.exception.InterpreterControlledException;
import com.yc.persistence.parser.QueryParser;

import br.edu.ufrn.loco3.Constant;
import br.edu.ufrn.loco3.dao.DAO;
import br.edu.ufrn.loco3.security.User;
import br.edu.ufrn.loco3.security.CredentialServiceImpl;
import br.edu.ufrn.loco3.security.UserlessAuthorizationServiceImpl;
import br.edu.ufrn.loco3.transformers.queries.Helper;
import br.edu.ufrn.loco3.transformers.queries.ReadStatementTransformation;
import br.edu.ufrn.loco3.transformers.queries.rmap.NoSQLRowMapper;
import br.edu.ufrn.loco3.transformers.queries.rmap.SQLRowMapper;

@Service
public class QueryServiceImpl implements DAO
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DBRDriverRemoteImpl dbrDriverRemote;

    @Autowired
    private DBNDriverRemoteImpl dbnDriverRemote;

    @Autowired
    private CacheRemoteImpl cacheRemoteImpl;

    /*@Autowired
    private LocalCacheImpl schemaCache;

    @Autowired
    private SQLStatementCache sqlStatementCacheWrapper;*/

    @Autowired
    private SchemaCacheServiceImpl schemaCacheService;

    public JSONArray readByCriteria(String traceId, String spanId, String parentSpanId, String tenantId, JSONArray datas, User user, Boolean isDataRequest) throws InterpreterControlledException, Exception
    {
        JSONArray response = new JSONArray();

        //logger.info(" > -1.0");

        for (int dataIdx = 0; dataIdx < datas.length(); dataIdx++)
        {
            //logger.info(" > -1.1");

            JSONObject data = datas.getJSONObject(dataIdx);

            //logger.info(" > -1.2");

            String contentHash = "";
            Boolean useCache = false;
            JSONObject sCache = null;
            if (data.has(Constant._cache))
            {
                sCache = new JSONObject(data.remove(Constant._cache).toString());
                if (sCache.has(Constant._behavior))
                {
                    if (sCache.getString(Constant._behavior).equals(Constant.use))
                    {
                        useCache = true;
                        contentHash = String.valueOf(data.toString().hashCode());
                        if (contentHash.startsWith("-")) 
                        {
                            contentHash = contentHash.replace("-", "n");
                        }
                        else
                        {
                            contentHash = "p".concat(contentHash);
                        }

                        String content = this.cacheRemoteImpl.read(traceId, spanId, parentSpanId, tenantId, contentHash);
                        if (content == null) 
                        {
                            /*
                             * load data and store in cache
                             * 
                             */
                        }
                        else
                        {
                            response.put(new JSONObject(content));

                            continue;
                        }
                    }
                    else if (sCache.getString(Constant._behavior).equals(Constant.evict))
                    {
                        this.cacheRemoteImpl.delete(traceId, spanId, parentSpanId, tenantId, String.valueOf(data.toString().hashCode()));

                        useCache = false;
                    }
                    else if (sCache.getString(Constant._behavior).equals(Constant.ignore))
                    {
                        useCache = false;
                    }
                    else
                    {
                        throw new Exception("400:cache unknown behavior.");
                    }
                }
            }

            //logger.info(" > -1.3");

            String sPaging = null;
            if (data.has(Constant._paging))
            {
                sPaging = data.remove(Constant._paging).toString();
            }
            else
            {
                JSONObject paging = new JSONObject();
                paging.put(Constant._maxRegisters, 500);
                paging.put(Constant._firstRegister, 0);

                sPaging = paging.toString();
            }

            //logger.info(" > -1.4");

            String sSorting = null;
            if (data.has(Constant._sorting))
            {
                sSorting = data.remove(Constant._sorting).toString();
            }
            else
            {
                JSONObject one = new JSONObject();
                one.put(Constant._orderBy, Constant.id);
                one.put(Constant._order, "ASC");

                JSONObject sorting = new JSONObject();
                sorting.put("0", one);
                sSorting = sorting.toString();
            }

            //logger.info(" > -1.5");

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

            //logger.info(" > -1.6");

            String entityName = data.keys().next();

            //logger.info(" > data: "+data);
            JSONArray queries = new QueryParser().parse(data);
            //logger.info(" > queries: "+queries);

            //logger.info(" > -1.7");

            for (int idx = 0; idx < queries.length(); idx++)
            {
                queries.getJSONObject(idx).put(Constant._paging, new JSONObject(sPaging));
                queries.getJSONObject(idx).put(Constant._sorting, new JSONObject(sSorting));
                queries.getJSONObject(idx).put(Constant._connective, sConnective);
                queries.getJSONObject(idx).put(Constant._count, sCount);

                //logger.info(" > -1.7.0");

                try
                {
                    JSONObject content = null;
                    if (user == null)
                    {
                        //logger.info(" > -1.7.0.0");

                        if (isDataRequest)
                        {
                            //logger.info(" > -1.7.0.0.0");

                            content = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), null);
                        }
                        else
                        {
                            //logger.info(" > -1.7.0.0.1");

                            content = this.getStatementByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), null);
                        }
                    }
                    else
                    {
                        //logger.info(" > -1.7.0.1");

                        //logger.info(" > user: "+user.getUsername());

                        //logger.info(" > isDataRequest: "+isDataRequest);

                        //logger.info(" > tenantId: "+tenantId);

                        if (isDataRequest)
                        {
                            //logger.info(" > -1.7.0.1.0");

                            content = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), user);
                        }
                        else
                        {
                            //logger.info(" > -1.7.0.1.1");

                            content = this.getStatementByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), user);
                        }
                    }

                    if (useCache) 
                    {
                        this.cacheRemoteImpl.update(traceId, spanId, parentSpanId, tenantId, contentHash, content.toString(), sCache.getLong(Constant._ttl));
                    }

                    //logger.info(" > a");

                    response.put(content);
                }
                catch (Exception e)
                {
                    //logger.info(" > b error");

                    e.printStackTrace();
                    
                    throw e;
                }
            }
        }

        return response;
    }

    public JSONArray readAltByCriteria(String traceId, String spanId, String parentSpanId, String tenantId, JSONObject data, User user, Boolean isDataRequest) throws InterpreterControlledException, Exception
    {
        JSONArray response = new JSONArray();

        String contentHash = "";
        Boolean useCache = false;
        JSONObject sCache = null;
        if (data.has(Constant._cache))
        {
            sCache = new JSONObject(data.remove(Constant._cache).toString());
            if (sCache.has(Constant._behavior))
            {
                if (sCache.getString(Constant._behavior).equals(Constant.use))
                {
                    useCache = true;
                    contentHash = String.valueOf(data.toString().hashCode());
                    if (contentHash.startsWith("-")) 
                    {
                        contentHash = contentHash.replace("-", "n");
                    }
                    else
                    {
                        contentHash = "p".concat(contentHash);
                    }

                    String content = this.cacheRemoteImpl.read(traceId, spanId, parentSpanId, tenantId, contentHash);
                    if (content == null) 
                    {
                        /*
                         * load data and store in cache
                         * 
                         */
                    }
                    else
                    {
                        response.put(new JSONObject(content));
                    }
                }
                else if (sCache.getString(Constant._behavior).equals(Constant.evict))
                {
                    this.cacheRemoteImpl.delete(traceId, spanId, parentSpanId, tenantId, String.valueOf(data.toString().hashCode()));

                    useCache = false;
                }
                else if (sCache.getString(Constant._behavior).equals(Constant.ignore))
                {
                    useCache = false;
                }
                else
                {
                    throw new Exception("400:cache unknown behavior.");
                }
            }
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
            JSONObject one = new JSONObject();
            one.put(Constant._orderBy, Constant.id);
            one.put(Constant._order, "ASC");

            JSONObject sorting = new JSONObject();
            sorting.put("0", one);
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
                    if (isDataRequest)
                    {
                        content = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), null);
                    }
                    else
                    {
                        content = this.getStatementByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), null);
                    }
                }
                else
                {
                    //logger.info(" > user: "+user.getUsername());

                    //logger.info(" > tenantId: "+tenantId);

                    //logger.info(" > isDataRequest: "+isDataRequest);

                    if (isDataRequest)
                    {
                        content = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), user);

                        //logger.info(" > content: "+content);
                    }
                    else
                    {
                        content = this.getStatementByCriteria(traceId, spanId, parentSpanId, tenantId, entityName, sCount, queries.getJSONObject(idx), user);   
                    }
                }

                if (useCache) 
                {
                    this.cacheRemoteImpl.update(traceId, spanId, parentSpanId, tenantId, contentHash, content.toString(), sCache.getLong(Constant._ttl));
                }

                response = content.getJSONArray(entityName);

                //response.put(content);
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        return response;
    }

    @Override
    public JSONObject readById(String traceId, String spanId, String parentSpanId, String tenantId, JSONObject jsObject) throws InterpreterControlledException, Exception
    {
        JSONArray jsArray = new JSONArray();
        jsArray.put(jsObject);
        jsArray = this.readByCriteria(traceId, spanId, parentSpanId, tenantId, jsArray, null, true);
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

        //logger.info(" > b error: 0");
        
        SchemaCacheWrapper schemaCacheWrapper = new SchemaCacheWrapper(this.schemaCacheService, traceId, spanId, parentSpanId, tenantId);

        //logger.info(" > b error: 1");
        
        if (user == null)
        {
            //logger.info(" > b error: 0.0");
            
            helper = new ReadStatementTransformation(schemaCacheWrapper)
                    .getReadRelationalStatement(tenantId, entityName, data, new UserlessAuthorizationServiceImpl(), new HashSet<>());
        }
        else
        {
            //logger.info(" > entityName: "+entityName);

            //logger.info(" > data: "+data);

            //logger.info(" > b error: 0.1");
            
            helper = new ReadStatementTransformation(schemaCacheWrapper)
                    .getReadRelationalStatement(tenantId, entityName, data, new CredentialServiceImpl(user), new HashSet<>());
        }

        //logger.info(" > helper: "+helper);
        
        if (helper.dbType.equals(Constant.nosql))
        {
            JSONArray datas = this.dbnDriverRemote.select(traceId, parentSpanId, parentSpanId, spanId, parentSpanId, tenantId, entityName);

            return new NoSQLRowMapper().getNoSQLData(data, entityName, helper.schema, datas, true);
        }

        if (helper.simpleQuery)
        {
            //logger.info(" > selectAsJSON: helper.query: "+helper.query);

            JSONObject object = new JSONObject();
            object.put(entityName, this.dbrDriverRemote.selectAsJSON(traceId, spanId, parentSpanId, tenantId, helper.query));

            //logger.info(" > object: "+object);

            return object;
        }
        else
        {
            //logger.info(" > select: helper.query: "+helper.query);

            JSONArray datas = this.dbrDriverRemote.select(traceId, spanId, parentSpanId, tenantId, helper.query);

            //logger.info(" > datas: "+datas);

            return new SQLRowMapper(schemaCacheWrapper).getDatas(tenantId, entityName, data, datas, count);
        }
    }

    private JSONObject getStatementByCriteria(String traceId, String spanId, String parentSpanId, String tenantId, String entityName, Boolean count, JSONObject data, User user) throws InterpreterControlledException, Exception
    {
        Helper helper = null;

        SchemaCacheWrapper schemaCacheWrapper = new SchemaCacheWrapper(this.schemaCacheService, traceId, spanId, parentSpanId, tenantId);

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

        JSONObject jsObject = new JSONObject();
        jsObject.put("statement", helper.query);
        return jsObject;
    }
}
