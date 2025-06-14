package com.yc.persistence.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yc.persistence.api.client.CacheRESTClient;

@Service
public class SchemaCacheServiceImpl 
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CacheRESTClient cacheService;

    public JSONObject create(String traceId, String spanId, String parentSpanId, String cacheResourceId, String content, Long expires) throws Exception 
    {
        final ResponseEntity<String> responseEntity = this.cacheService.create(traceId, spanId, parentSpanId, cacheResourceId, expires, content);
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) 
        {
            JSONObject response = new JSONObject();
            response.put("status", true);
            response.put("cacheResourceId", cacheResourceId);
            return response;
        }
        else 
        {
            JSONObject response = new JSONObject(responseEntity.getBody());
            response.put("status", false);
            return response;
        }
    }

    public JSONObject update(String traceId, String spanId, String parentSpanId, String cacheResourceId, String content, Long expires) throws Exception 
    {
        final ResponseEntity<String> responseEntity = this.cacheService.update(traceId, spanId, parentSpanId, cacheResourceId, expires, content);
        if (responseEntity.getStatusCode() == HttpStatus.OK) 
        {
            JSONObject response = new JSONObject();
            response.put("status", true);
            return response;
        }
        else 
        {
            JSONObject response = new JSONObject(responseEntity.getBody());
            response.put("status", false);
            return response;
        }
    }

    public JSONObject read(String traceId, String spanId, String parentSpanId, String cacheResourceId) throws Exception 
    {
        //logger.info(" > cacheResourceId: "+cacheResourceId);

        final ResponseEntity<String> responseEntity = this.cacheService.read(traceId, spanId, parentSpanId, cacheResourceId);
        if (responseEntity.getStatusCode() == HttpStatus.OK) 
        {
            //logger.info(" > responseEntity.getStatusCode(): "+responseEntity.getStatusCode());

            JSONObject response = new JSONObject();
            response.put("status", true);
            response.put("content", new JSONObject(responseEntity.getBody()));
            return response;
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) 
        {
            JSONObject response = new JSONObject();
            response.put("status", true);
            return response;
        }
        else 
        {
            JSONObject response = new JSONObject(responseEntity.getBody());
            response.put("status", false);
            return response;
        }
    }

    public JSONObject exists(String traceId, String spanId, String parentSpanId, String cacheResourceId) throws Exception 
    {
        final ResponseEntity<String> responseEntity = this.cacheService.exists(traceId, spanId, parentSpanId, cacheResourceId);
        if (responseEntity.getStatusCode() == HttpStatus.OK) 
        {
            JSONObject response = new JSONObject();
            response.put("status", true);
            return response;
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) 
        {
            JSONObject response = new JSONObject();
            response.put("status", false);
            return response;
        }
        else 
        {
            JSONObject response = new JSONObject(responseEntity.getBody());
            response.put("status", false);
            return response;
        }
    }

    public JSONObject delete (String traceId, String spanId, String parentSpanId, String cacheResourceId) throws Exception 
    {
        final ResponseEntity<String> responseEntity = this.cacheService.delete(traceId, spanId, parentSpanId, cacheResourceId);
        if (responseEntity.getStatusCode() == HttpStatus.OK) 
        {
            JSONObject response = new JSONObject();
            response.put("status", true);
            return response;
        }
        else 
        {
            JSONObject response = new JSONObject(responseEntity.getBody());
            response.put("status", false);
            return response;
        }
    }
}