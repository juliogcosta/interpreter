package com.yc.persistence.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@FeignClient(name = "${app.dbnrdriver.name}")
public interface DBNDriverRESTClient
{
    final static String BASE = "/a2w34flkj9/unsecured/org/{orgName}/project/{projectName}";

    final static String POST = "POST";
    final static String GET = "GET";
    final static String PUT = "PUT";
    final static String DELETE = "DELETE";

    @RequestLine(GET+" "+BASE+"/ddl/keyspace/{keyspaceName}/and-cqlsttmnt")
    @Headers(value = { "Accept: application/json", 
            "x-b3-traceid: {traceId}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> getDetailedKeyspaceByNameAndCQLSTTMNT(
            @Param("traceId") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("orgName") String orgName, 
            @Param("projectName") String projectName, 
            @Param("keyspaceName") String keyspaceName) throws Exception;

    @RequestLine(POST+" /h3u70axu4of/unsecured/org/{orgName}/project/{projectName}/keyspace/{keyspaceName}/schema/onrm/insert")
    @Headers(value = { "Content-Type: application/json", 
            "Accept: application/json", 
            "x-b3-traceid: {traceId}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> insertData(
            @Param("traceId") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("orgName") String orgName, 
            @Param("projectName") String projectName, 
            @Param("keyspaceName") String keyspaceName, 
            @RequestBody(required = true) final String body);

    @RequestLine(POST+" /h3u70axu4of/unsecured/org/{orgName}/project/{projectName}/keyspace/{keyspaceName}/schema/onrm/select")
    @Headers(value = { "Content-Type: application/json", 
            "Accept: application/json", 
            "x-b3-traceid: {traceId}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> readData(
            @Param("traceId") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("orgName") String orgName, 
            @Param("projectName") String projectName, 
            @Param("keyspaceName") String keyspaceName, 
            @RequestBody(required = true) final String body);

    @RequestLine(POST+" /h3u70axu4of/unsecured/org/{orgName}/project/{projectName}/keyspace/{keyspaceName}/schema/onrm/update")
    @Headers(value = { "Content-Type: application/json", 
            "Accept: application/json", 
            "x-b3-traceid: {traceId}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> updateData(
            @Param("traceId") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId, 
            @Param("orgName") String orgName, 
            @Param("projectName") String projectName, 
            @Param("keyspaceName") String keyspaceName, 
            @RequestBody(required = true) final String body);

    @RequestLine(POST+" /h3u70axu4of/unsecured/org/{orgName}/project/{projectName}/keyspace/{keyspaceName}/schema/onrm/delete")
    @Headers(value = { "Content-Type: application/json", 
            "Accept: application/json", 
            "x-b3-traceid: {traceId}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> deleteData(
            @Param("traceId") String traceId, 
            @Param("spanId") String spanId, 
            @Param("parentSpanId") String parentSpanId,
            @Param("orgName") String orgName, 
            @Param("projectName") String projectName, 
            @Param("keyspaceName") String keyspaceName, 
            @RequestBody(required = true) final String body);
}
