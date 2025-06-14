package com.yc.persistence.filter;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.yc.error.ErrorLogger;
import com.yc.models.sql.up.Project;
import com.yc.persistence.PersistenceApplication;
import com.yc.persistence.api.client.DBRDriverRESTClient;
import com.yc.persistence.api.client.ForgerRESTClient;
import com.yc.persistence.service.QueryServiceImpl;
import com.yc.persistence.service.SchemaCacheServiceImpl;
import com.yc.utils.ContextManagerControl;

import br.edu.ufrn.loco3.Constant;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class URLFilter implements Filter
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    private String appName;

    public Boolean logDisplay;

    public Long ttl;

    private String contractXTenantID;

    private ForgerRESTClient forgerRESTClient;

    private DBRDriverRESTClient sqlDriverRESTClient;

    private ContextManagerControl contextManagerControl;

    private ErrorLogger errorLogger = null;

    private QueryServiceImpl queryService;

    private SchemaCacheServiceImpl schemaCacheService;

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public void setLogDisplay(Boolean logDisplay)
    {
        this.logDisplay = logDisplay;
    }

    public void setTtl(Long ttl)
    {
        this.ttl = ttl;
    }

    public void setContractXTenantID(String contractXTenantID)
    {
        this.contractXTenantID = contractXTenantID;
    }

    public void setForgerRESTClient(ForgerRESTClient forgerRESTClient)
    {
        this.forgerRESTClient = forgerRESTClient;
    }

    public void setSQLDriverRESTClient(DBRDriverRESTClient sqlDriverRESTClient)
    {
        this.sqlDriverRESTClient = sqlDriverRESTClient;
    }

    public void setQueryService(QueryServiceImpl queryService)
    {
        this.queryService = queryService;
    }

    public void setSchemaCacheService(SchemaCacheServiceImpl schemaCacheService)
    {
        this.schemaCacheService = schemaCacheService;
    }

    public void setContextManagerControl(ContextManagerControl contextManagerControl)
    {
        this.contextManagerControl = contextManagerControl;
    }

    public void setErrorLogger(ErrorLogger errorLogger)
    {
        this.errorLogger = errorLogger;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, 
            ServletResponse servletResponse, 
            FilterChain filterChain) throws IOException, ServletException
    {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String tenantId = request.getHeader("X-Tenant-Id");

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        final JSONObject validTenantId = new JSONObject();
        validTenantId.put("validTenantId", false);

        try
        {
            //logger.info(" > tenantId: "+tenantId);
            JSONObject cache = this.schemaCacheService.exists(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantId));
            //logger.info(" > cache: "+cache.toString());

            if (tenantId == null) 
            {
                throw new Exception("400:Cabeçalho X-Tenant-Id está ausente.");
            }
            else if (xB3TraceId == null) 
            {
                throw new Exception("400:Cabeçalho x-b3-traceid está ausente.");
            }
            else if (cache.getBoolean("status")) 
            {
                //logger.info(" > tenantId already exist.");
            }
            else 
            {
                this.loadSchema(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, request.getMethod(), request.getRequestURI(), false, validTenantId);
            }

            if (this.contextManagerControl.containsKey(tenantId)) 
            {
                
            }
            else 
            {
                this.setContextManagerControl(tenantId);
            }

            filterChain.doFilter(request, servletResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            if (!validTenantId.getBoolean("validTenantId") || tenantId == null)
            {
                JSONObject jsError = new JSONObject();
                jsError.put("message", e.getMessage());
                response.getWriter().write(jsError.toString());
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
            else
            {
                Boolean containsKey = this.contextManagerControl.containsKey(tenantId);
                if (containsKey) 
                {
                    if (this.contextManagerControl.getLogConsole(tenantId)) 
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    e.printStackTrace();
                }

                JSONObject jsError = this.errorLogger.buildErrorMessage("yc.persistence-q", 
                        e, 
                        tenantId, 
                        xB3TraceId, 
                        xB3SpanId, 
                        xB3ParentSpanId, 
                        null, 
                        null, 
                        null, 
                        this.appName, 
                        "URLFilter", 
                        request.getMethod(), 
                        null, 
                        null, 
                        "i",
                        false);

                response.getWriter().write(jsError.toString());
                response.setStatus(Integer.parseInt(jsError.getString("code")));
            }
        }
    }

    @Override
    public void destroy()
    {

    }

    public void loadSchema(String traceId,
            String spanId,
            String parentSpanId,
            String tenantId, 
            String httpMethod, 
            String httpEndpoint,
            Boolean isContract,
            JSONObject validTenantId) throws Exception
    {
        ///org/project/dataschema/tenant-id/{tenantId}
        //logger.info(" > traceId: "+traceId);
        //logger.info(" > spanId: "+spanId);
        //logger.info(" > parentSpanId: "+parentSpanId);
        //logger.info(" > tenantId: "+tenantId);
        ResponseEntity<String> responseEntity = this.forgerRESTClient.getDataSchemaByTenantId(traceId, spanId, parentSpanId, tenantId);

        //logger.info(" > responseEntity.statusCode: "+responseEntity.getStatusCode());
        //logger.info(" > responseEntity.body: "+responseEntity.getBody());

        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            validTenantId.put("validTenantId", true);

            JSONObject jsDataSchema = new JSONObject(responseEntity.getBody());
            if (!jsDataSchema.getString("status").equals(Project.State.RUNNING.name()))
            {
                throw new Exception("400:Esse schema de dados não está disponível para ser executado.");
            }

            //logger.info(" > jsDataSchema: "+jsDataSchema.toString());

            //logger.info(" > jsDataSchema: "+jsDataSchema);

            JSONObject deployedEntities = null;

            responseEntity = this.sqlDriverRESTClient.getEntitiesDeployedToDataSchema(traceId, spanId, parentSpanId, tenantId);
            //logger.info(" > responseEntity.statusCode: "+responseEntity.getStatusCode());

            if (responseEntity.getStatusCode() == HttpStatus.OK)
            {
                //logger.info(" > responseEntity.body: "+responseEntity.getBody());

                deployedEntities = new JSONObject(responseEntity.getBody());
            }
            else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                throw new Exception(String.valueOf(responseEntity.getStatusCode().value()).concat(":O schema de dados não foi localizado."));
            }
            else if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                throw new Exception(String.valueOf(responseEntity.getStatusCode().value()).concat(":O schema de dados não foi localizado."));
            }
            else
            {
                throw new Exception(String.valueOf(responseEntity.getStatusCode().value()).concat(": for tenant '")
                        .concat(tenantId).concat("': ").concat(responseEntity.getBody() == null ? "unavailable message." : responseEntity.getBody()));
            }

            if (PersistenceApplication.nosql)
            {
                /*
                responseEntity = this.noSQLDriverRESTClient
                        .getSchema(tenantId,jsProject.getString("owner").concat("_").concat(jsProject.getString("name")));
                if (responseEntity.getStatusCode() == HttpStatus.OK)
                {
                    noSQLSchema = new JSONObject(responseEntity.getBody());
                    if (noSQLSchema.length() > 0)
                    {
                        noSQLSchema.getJSONObject(Constant.ooStts).keySet().forEach(entityName -> {
                            schema.getJSONObject(Constant.ooStts).put(entityName, 
                                    noSQLSchema.getJSONObject(Constant.ooStts).getJSONObject(entityName));
                        });

                        noSQLSchema.getJSONObject(Constant.nonRelationalStts).keySet().forEach(entityName -> {
                            schema.getJSONObject(Constant.relationalStts).put(entityName, 
                                    noSQLSchema.getJSONObject(Constant.nonRelationalStts).getJSONObject(entityName));
                        });
                    }
                }
                else
                {
                    final String failMessage = String.valueOf(HttpStatus.EXPECTATION_FAILED.value())
                            .concat(":failed to retrieve project schema nosql for this tenantId '").concat(tenantId).concat("'. ");
                    throw new Exception(this.registerError(new Exception(failMessage), "", tenantId, 
                                            method, uri, 400, URLFilter.class).getString("message"));
                }
                 */
            }

            /*
            if (isContract) 
            {
                this.addSchema(tenantId, 
                        jsProject.getString(Constant.name), 
                        schema, 
                        jsProject.getString("tenantSecret"), 
                        1000L*60*60*1);

                this.errorControl.initKey(tenantId, this.ttl, false);
                this.errorControl.setLogConsole(tenantId, this.logDisplay);
            }
            else
            {
                JSONObject jsContract = this.getContract(id, spanId, parentSpanId, jsProject.getString("owner"), httpMethod, httpEndpoint);
                */

                /*final String dbSchemaName = jsDataSchema.getJSONObject("project").getString("owner")
                            .concat(".").concat(jsDataSchema.getJSONObject("project").getString("name"))
                            .concat(".").concat(jsDataSchema.getString(Constant.name))*/

            //logger.info(" > secret: "+jsDataSchema.getJSONObject("project").getString("secret"));

            final String dbSchemaName = jsDataSchema.getString(Constant.name);
            this.addDataSchema(traceId, spanId, parentSpanId, tenantId, 
                        dbSchemaName, 
                        deployedEntities, 
                        jsDataSchema.getJSONObject("project").getString("secret"), 
                        1000L*60*60*1);

            /*Boolean saveLog = jsContract.getJSONObject("plan").getString("name").equalsIgnoreCase("PRO") 
                        || jsContract.getJSONObject("plan").getString("name").equalsIgnoreCase("ENTERPRISE");*/
            
            //}
        }
        else
        {
            throw new Exception(String.valueOf(responseEntity.getStatusCode().value()).concat(":Tenant '")
                    .concat(tenantId).concat("': ").concat(
                            responseEntity.getBody() == null ? "Unavailable message." : responseEntity.getBody()));
        }
    }

    private void setContextManagerControl(String tenantId) throws Exception
    {
        Boolean saveLog = false;

        this.contextManagerControl.initKey(tenantId, this.ttl, false);
        this.contextManagerControl.setLogConsole(tenantId, this.logDisplay);
        this.contextManagerControl.setSaveLog(tenantId, saveLog);
    }

    private void addDataSchema(String traceId, String spanId, String parentSpanId, String tenantId, 
            String schemaName, 
            JSONObject schemaEntities, 
            String tenantSecret, 
            Long ttl) throws Exception
    {
        JSONObject schema = new JSONObject();
        schema.put("schema", new JSONObject());
        schema.getJSONObject("schema").put("name", schemaName);
        schema.getJSONObject("schema").put("entities", schemaEntities);

        this.schemaCacheService.update(traceId, spanId, parentSpanId, "persistence-service-".concat(tenantId), schema.toString(), ttl);

        this.schemaCacheService.update(traceId, spanId, parentSpanId, "persistence-service-".concat(tenantSecret), tenantId, ttl);
        
        //logger.info(" > add dataSchema!");
    }

    @SuppressWarnings("unused")
    private JSONObject getContract(String traceId,
            String spanId,
            String parentSpanId,
            String orgName, 
            String httpMethod, 
            String httpEndpoint) throws Exception
    {
        JSONObject _jsContract = new JSONObject();
        _jsContract.put("id", "");
        _jsContract.put("orgname", orgName);
        _jsContract.put("status", "");
        _jsContract.put("uuid", "");
        _jsContract.put("hiredin", "");
        _jsContract.put("expiresin", "");
        _jsContract.put("logversion", "");
        _jsContract.put("plan", new JSONObject());
        _jsContract.getJSONObject("plan").put("id", "");
        _jsContract.getJSONObject("plan").put("name", "");
        _jsContract.getJSONObject("plan").put("ttl", "");
        _jsContract.getJSONObject("plan").put("threshold", "");
        _jsContract.getJSONObject("plan").put("ppo", "");
        _jsContract.getJSONObject("plan").put("logversion", "");
        JSONObject jsData = new JSONObject();
        jsData.put("contract", _jsContract);

        JSONArray jsQuery = new JSONArray();
        jsQuery.put(jsData);

        JSONObject jsContract = null;

        /*
        if (!this.schemaCacheService.containsKey(this.contractXTenantID))
        {
            JSONObject validTenantId = new JSONObject();
            validTenantId.put("validTenantId", true);
            this.loadSchema(id, spanId, parentSpanId, this.contractXTenantID, httpMethod, httpEndpoint, true, validTenantId);
        }
         */

        JSONArray datas = this.queryService.readByCriteria(traceId, spanId, parentSpanId, this.contractXTenantID, jsQuery, null, true);

        JSONArray jsContracts = datas.getJSONObject(0).getJSONArray("contract");

        int count = 0;

        for (int idx = 0; idx < jsContracts.length(); idx++)
        {
            if (jsContracts.getJSONObject(idx).getString("status").equals("ACTIVE"))
            {
                jsContract = jsContracts.getJSONObject(idx);

                count++;
            }

            if (count > 1) 
            {
                throw new Exception("510:Esta org '".concat(orgName).concat("' não tem contrato ativo."));
            }
        }

        if (jsContract == null)
        {
            throw new Exception("204:contract not found");
        }

        return jsContract;
    }
}
