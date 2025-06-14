package com.yc.persistence.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@FeignClient(name = "${app.cache.name}")
public interface CacheRESTClient
{
    final static public String urlPrefix = "/as4pc0mnk8z/unsecured/stm";

    @RequestLine("POST "+CacheRESTClient.urlPrefix+"/content/expires/{expires}")
    @Headers(value = { "Content-Type: application/json", 
            "x-cache-resource-id: {cacheResourceId}", 
            "x-b3-traceid: {id}", 
            "x-b3-spanid: {spanId}", 
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> create (
            @Param("id") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("cacheResourceId") String cacheResourceId, 
            @Param("expires") Long expires, 
            @RequestBody String body) throws Exception;

    @RequestLine("PUT "+CacheRESTClient.urlPrefix+"/content/expires/{expires}")
    @Headers(value = { "Content-Type: application/json", 
            "x-cache-resource-id: {cacheResourceId}", 
            "x-b3-traceid: {id}", 
            "x-b3-spanid: {spanId}", 
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> update (
            @Param("id") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("cacheResourceId") String cacheResourceId, 
            @Param("expires") Long expires, 
            @RequestBody String body) throws Exception;

    @RequestLine("GET "+CacheRESTClient.urlPrefix+"/content")
    @Headers(value = { "x-cache-resource-id: {cacheResourceId}", 
            "x-b3-traceid: {id}", 
            "x-b3-spanid: {spanId}", 
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> read (
            @Param("id") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("cacheResourceId") String cacheResourceId) throws Exception;

    @RequestLine("GET "+CacheRESTClient.urlPrefix+"/content/exists")
    @Headers(value = { "x-cache-resource-id: {cacheResourceId}", 
            "x-b3-traceid: {id}", 
            "x-b3-spanid: {spanId}", 
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> exists (
            @Param("id") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("cacheResourceId") String cacheResourceId) throws Exception;

    @RequestLine("DELETE "+CacheRESTClient.urlPrefix+"/content")
    @Headers(value = { "x-cache-resource-id: {cacheResourceId}", 
            "x-b3-traceid: {id}", 
            "x-b3-spanid: {spanId}", 
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> delete (
            @Param("id") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("cacheResourceId") String cacheResourceId) throws Exception;
}