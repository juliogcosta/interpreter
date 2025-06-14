package com.yc.persistence.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@FeignClient(name = "${app.forger.name}")
public interface ForgerRESTClient
{
    static final String urlPrefix = "/a35gry2b4d8ops/unsecured";

    @RequestLine("GET "+ForgerRESTClient.urlPrefix+"/org/project/dataschema/tenant-id/{tenantId}")
    @Headers(value = { "Accept: application/json",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> getDataSchemaByTenantId(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId) throws Exception;
}