package com.yc.persistence.api.client;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("SQLDriverRemoteImpl")
public class DBRDriverRemoteImpl
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DBRDriverRESTClient clientRest;

    public JSONArray select(String traceId, String spanId, String parentSpanId, String tenantId, String sqlQuery) throws Exception
    {
        final ResponseEntity<String> responseEntity = this.clientRest.dsSelect(traceId, spanId, parentSpanId, tenantId, sqlQuery);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            return new JSONObject(responseEntity.getBody()).getJSONArray("data");
        }
        else
        {
            throw new Exception(responseEntity.getBody());
        }
    }

    public JSONArray selectAsJSON(String traceId, String spanId, String parentSpanId, String tenantId, String sqlQuery) throws Exception
    {
        final ResponseEntity<String> responseEntity = this.clientRest.dsSelectAsJSON(traceId, spanId, parentSpanId, tenantId, sqlQuery);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            return new JSONObject(responseEntity.getBody()).getJSONArray("data");
        }
        else
        {
            throw new Exception(responseEntity.getBody());
        }
    }

    public JSONArray selectAsMatrix(String traceId, String spanId, String parentSpanId, String tenantId, String sqlQuery) throws Exception
    {
        final ResponseEntity<String> responseEntity = this.clientRest.dsSelectAsMatrix(traceId, spanId, parentSpanId, tenantId, sqlQuery);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            return new JSONObject(responseEntity.getBody()).getJSONArray("data");
        }
        else
        {
            throw new Exception(responseEntity.getBody());
        }
    }

    public String toInsert(String traceId, String spanId, String parentSpanId, String tenantId, String data) throws Exception
    {
        final ResponseEntity<String> responseEntity = this.clientRest.dsInsert(traceId, spanId, parentSpanId, tenantId, data.toString());
        if (responseEntity.getStatusCode() == HttpStatus.CREATED)
        {
            return responseEntity.getBody();
        }
        else
        {
            throw new Exception(responseEntity.getBody());
        }
    }

    public void toUpdate(String traceId, String spanId, String parentSpanId, String tenantId, String data) throws Exception
    {
        final ResponseEntity<String> responseEntity = this.clientRest.dsUpdate(traceId, spanId, parentSpanId, tenantId, data.toString());
        if (responseEntity.getStatusCode() != HttpStatus.OK)
        {
            throw new Exception(responseEntity.getBody());
        }
    }

    public void toDelete(String traceId, String spanId, String parentSpanId, String tenantId, String data) throws Exception
    {
        final ResponseEntity<String> responseEntity = this.clientRest.dsDelete(traceId, spanId, parentSpanId, tenantId, data.toString());
        if (responseEntity.getStatusCode() != HttpStatus.OK)
        {
            throw new Exception(responseEntity.getBody());
        }
    }

    /*
     * @Override public void transact(String projectOwner, String projectName,
     * JSONObject data) throws Exception { final ResponseEntity<String>
     * responseEntity = this.clientRest.transact(projectOwner, projectName,
     * data.toString()); if (responseEntity.getStatusCode() != HttpStatus.OK) {
     * throw new Exception(responseEntity.getBody()); } }
     */
}
