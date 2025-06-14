package com.yc.persistence.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("CacheRemoteImpl")
public class CacheRemoteImpl
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    /**
    @Autowired
    private CacheRESTClient clientRest;
     */

    public void update(String traceId, String spanId, String parentSpanId, String tenantId, String contentHash, String content, Long expires) throws Exception
    {
        /*
        final ResponseEntity<String> responseEntity = this.clientRest.update(id, spanId, parentSpanId, tenantId, contentHash, expires, content);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            
        }
        else
        {
            throw new Exception(responseEntity.getBody());
        }
         */
    }

    public String read(String traceId, String spanId, String parentSpanId, String tenantId, String contentHash) throws Exception
    {
        /*
        final ResponseEntity<String> responseEntity = this.clientRest.read(id, spanId, parentSpanId, tenantId, contentHash);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            return responseEntity.getBody();
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT)
        {
            return null;
        }
        else
        {
            throw new Exception(responseEntity.getBody());
        }
         */
        return null;
    }

    public void delete(String traceId, String spanId, String parentSpanId, String tenantId, String contentHash) throws Exception
    {
        /*
        final ResponseEntity<String> responseEntity = this.clientRest.delete(id, spanId, parentSpanId, tenantId, contentHash);
        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            
        }
        else
        {
            throw new Exception(responseEntity.getBody());
        }
         */
    }
}
