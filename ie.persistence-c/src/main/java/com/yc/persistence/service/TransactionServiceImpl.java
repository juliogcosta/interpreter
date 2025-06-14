package com.yc.persistence.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yc.loco3.Constant;
import com.yc.loco3.security.CredentialServiceService;
import com.yc.loco3.transformers.mutations.CreateStatementTransformation;
import com.yc.loco3.transformers.mutations.DeleteStatementTransformation;
import com.yc.loco3.transformers.mutations.UpdateStatementTransformation;
import com.yc.persistence.api.client.DBNDriverRemoteImpl;
import com.yc.persistence.api.client.DBRDriverRemoteImpl;
import com.yc.persistence.exception.InterpreterControlledException;

@Service
public class TransactionServiceImpl
{
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private QueryServiceImpl queryService;
    
    @Autowired
    private DBRDriverRemoteImpl dbrDriverRemote;
    
    @Autowired
    private DBNDriverRemoteImpl dbnDriverRemote;
    
    @Autowired
    private SchemaCacheServiceImpl schemaCacheService;
    
    public void create(String traceId, String spanId, String parentSpanId, String tenantId, JSONArray datas, CredentialServiceService userAuthorizationService) throws InterpreterControlledException, Exception
    {
        SchemaCacheWrapper wrapper = new SchemaCacheWrapper(this.schemaCacheService, traceId, spanId, parentSpanId, tenantId);
        CreateStatementTransformation transformer = new CreateStatementTransformation(wrapper);
        
        JSONObject sqlStatements = new JSONObject();
        JSONObject noSQLStatements = new JSONObject();
        for (int idx = 0; idx < datas.length(); idx++)
        {
            JSONObject statement = transformer.getStatement(tenantId, datas.getJSONObject(idx), userAuthorizationService);
            if (statement.getString(Constant.dbType).equals(Constant.sql))
            {
                sqlStatements.put(String.valueOf(idx), statement);
            }
            else
            {
                noSQLStatements.put(String.valueOf(idx), statement);
            }
        }
        
        if (sqlStatements.length() > 0)
        {
            final JSONObject executedSQLStatements = new JSONObject(this.dbrDriverRemote.toInsert(traceId, spanId, parentSpanId, tenantId, sqlStatements.toString()).toString());
            for (String executedSQLStatementKey : executedSQLStatements.keySet())
            {
                JSONObject data = datas.getJSONObject(Integer.parseInt(executedSQLStatementKey));
                this.setDataTreeIdValues(executedSQLStatements.getJSONObject(executedSQLStatementKey), data);
            }
        }
        
        if (noSQLStatements.length() > 0)
        {
            datas = new JSONArray();
            String orgName = "", projectName = "", keyspaceName = "";
            this.dbnDriverRemote.toInsert(traceId, spanId, parentSpanId, orgName, projectName, keyspaceName, noSQLStatements.toString());
        }
    }
    
    public JSONObject getCreateStatement(String tenantId, JSONArray datas, CredentialServiceService userAuthorizationService) throws InterpreterControlledException, Exception
    {
        SchemaCacheWrapper wrapper = new SchemaCacheWrapper(this.schemaCacheService, tenantId, tenantId, tenantId, tenantId);
        CreateStatementTransformation transformer = new CreateStatementTransformation(wrapper);
        
        JSONObject sqlStatements = new JSONObject();
        JSONObject noSQLStatements = new JSONObject();
        for (int idx = 0; idx < datas.length(); idx++)
        {
            JSONObject statement = transformer.getStatement(tenantId, datas.getJSONObject(idx), userAuthorizationService);
            if (statement.getString(Constant.dbType).equals(Constant.sql))
            {
                sqlStatements.put(String.valueOf(idx), statement);
            }
            else
            {
                noSQLStatements.put(String.valueOf(idx), statement);
            }
        }
        
        return sqlStatements;
    }
    
    public void update(String traceId, String spanId, String parentSpanId, String tenantId, JSONArray datas, CredentialServiceService userAuthorizationService) throws InterpreterControlledException, Exception
    {
        SchemaCacheWrapper wrapper = new SchemaCacheWrapper(this.schemaCacheService, traceId, spanId, parentSpanId, tenantId);
        UpdateStatementTransformation transformer = new UpdateStatementTransformation(wrapper, this.queryService);
        
        JSONObject sqlStatements = new JSONObject();
        JSONObject noSQLStatements = new JSONObject();
        for (int idx = 0; idx < datas.length(); idx++)
        {
            JSONObject statement = transformer.getStatement(traceId, spanId, parentSpanId, tenantId, datas.getJSONObject(idx), userAuthorizationService);
            if (statement.getString(Constant.dbType).equals(Constant.sql))
            {
                sqlStatements.put(String.valueOf(idx), statement);
            }
            else
            {
                noSQLStatements.put(String.valueOf(idx), statement);
            }
        }
        
        if (sqlStatements.length() > 0)
        {
            this.dbrDriverRemote.toUpdate(traceId, spanId, parentSpanId, tenantId, sqlStatements.toString());
        }
        
        if (noSQLStatements.length() > 0)
        {
            String orgName = "", projectName = "", keyspaceName = "";
            this.dbnDriverRemote.toUpdate(traceId, spanId, parentSpanId, orgName, projectName, keyspaceName, noSQLStatements.toString());
        }
    }
    
    public JSONObject getUpdateStatements(String traceId, String spanId, String parentSpanId, String tenantId, JSONArray datas, CredentialServiceService userAuthorizationService) throws InterpreterControlledException, Exception
    {
        SchemaCacheWrapper wrapper = new SchemaCacheWrapper(this.schemaCacheService, traceId, spanId, parentSpanId, tenantId);
        UpdateStatementTransformation transformer = new UpdateStatementTransformation(wrapper, this.queryService);
        
        JSONObject sqlStatements = new JSONObject();
        JSONObject noSQLStatements = new JSONObject();
        for (int idx = 0; idx < datas.length(); idx++)
        {
            JSONObject sqlStatement = transformer.getStatement(traceId, spanId, parentSpanId, tenantId, datas.getJSONObject(idx), userAuthorizationService);
            if (sqlStatement.getString(Constant.dbType).equals(Constant.sql))
            {
                sqlStatements.put(String.valueOf(idx), sqlStatement);
            }
            else
            {
                noSQLStatements.put(String.valueOf(idx), sqlStatement);
            }
        }
        
        return sqlStatements;
    }
    
    public void delete(String traceId, String spanId, String parentSpanId, String tenantId, JSONArray datas, CredentialServiceService userAuthorizationService) throws InterpreterControlledException, Exception
    {
        SchemaCacheWrapper wrapper =new SchemaCacheWrapper(this.schemaCacheService, traceId, spanId, parentSpanId, tenantId);
        DeleteStatementTransformation transformer = new DeleteStatementTransformation(wrapper);
        
        JSONObject sqlStatements = new JSONObject();
        JSONObject noSQLStatements = new JSONObject();
        for (int idx = 0; idx < datas.length(); idx++)
        {
            JSONObject statement = transformer.getStatement(tenantId, datas.getJSONObject(idx), userAuthorizationService);
            if (statement.getString(Constant.dbType).equals(Constant.sql))
            {
                sqlStatements.put(String.valueOf(idx), statement);
            }
            else
            {
                noSQLStatements.put(String.valueOf(idx), statement);
            }
        }
        
        if (sqlStatements.length() > 0)
        {
            this.dbrDriverRemote.toDelete(traceId, spanId, parentSpanId, tenantId, sqlStatements.toString());
        }
        
        if (noSQLStatements.length() > 0)
        {
            String orgName = "", projectName = "", keyspaceName = "";
            this.dbnDriverRemote.toDelete(traceId, spanId, parentSpanId, orgName, projectName, keyspaceName, noSQLStatements.toString());
        }
    }
    
    public JSONObject getDeleteStatements(String tenantId, JSONArray datas, CredentialServiceService userAuthorizationService) throws InterpreterControlledException, Exception
    {
        SchemaCacheWrapper wrapper = new SchemaCacheWrapper(this.schemaCacheService, tenantId, tenantId, tenantId, tenantId);
        DeleteStatementTransformation transformer = new DeleteStatementTransformation(wrapper);
        
        JSONObject sqlStatements = new JSONObject();
        JSONObject noSQLStatements = new JSONObject();
        for (int idx = 0; idx < datas.length(); idx++)
        {
            JSONObject statement = transformer.getStatement(tenantId, datas.getJSONObject(idx), userAuthorizationService);
            if (statement.getString(Constant.dbType).equals(Constant.sql))
            {
                sqlStatements.put(String.valueOf(idx), statement);
            }
            else
            {
                noSQLStatements.put(String.valueOf(idx), statement);
            }
        }
        
        return sqlStatements;
    }
    
    private void setDataTreeIdValues(JSONObject dataFromDs, JSONObject data)
    {
        dataFromDs.keySet().forEach(attributeName -> {
            if (Constant.id.equals(attributeName)) {
                data.getJSONObject(data.keys().next()).put(Constant.id, dataFromDs.getLong(Constant.id));
            } else {
                setDataTreeIdValues(dataFromDs.getJSONObject(attributeName), data.getJSONObject(attributeName));
            }
        });
    }
        
    public void executeSqlStatements(String traceId, String spanId, String parentSpanId, String tenantId, JSONObject sqlStatements) throws Exception
    {
        this.dbrDriverRemote.executeStatements(traceId, spanId, parentSpanId, tenantId, sqlStatements.toString());
    }
}
