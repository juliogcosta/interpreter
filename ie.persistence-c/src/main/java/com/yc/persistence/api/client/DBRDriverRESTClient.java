package com.yc.persistence.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@FeignClient(name = "${app.dbrdriver.name}")
public interface DBRDriverRESTClient
{
    @RequestLine("POST /vw456d2kjf1re/unsecured/schema/orm/insert")
    @Headers(value = { "X-Tenant-Id: {tenantId}", 
            "Content-Type: application/json", 
            "Accept: application/json", 
            "x-b3-traceid: {id}", 
            "x-b3-spanid: {spanId}", 
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> dsInsert(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId, 
            @RequestBody(required = true) final String body) throws Exception;

    @RequestLine("POST /vw456d2kjf1sb/unsecured/schema/select")
    @Headers(value = { "X-Tenant-Id: {tenantId}", 
            "Content-Type: application/json", 
            "Accept: application/json",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> dsSelect(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId, 
            @RequestBody(required = true) final String sqlQuery) throws Exception;

    @RequestLine("POST /vw456d2kjf1sb/unsecured/schema/select/as-json")
    @Headers(value = { "X-Tenant-Id: {tenantId}", 
            "Content-Type: application/json", 
            "Accept: application/json",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> dsSelectAsJSON(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId, 
            @RequestBody(required = true) final String sqlQuery) throws Exception;

    @RequestLine("POST /vw456d2kjf1sb/unsecured/schema/select/as-matrix")
    @Headers(value = { "X-Tenant-Id: {tenantId}", 
            "Content-Type: application/json", 
            "Accept: application/json",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> dsSelectAsMatrix(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId, 
            @RequestBody(required = true) final String sqlQuery) throws Exception;

    @RequestLine("POST /aw4n6d2kjf1uq/unsecured/schema/orm/update")
    @Headers(value = { "X-Tenant-Id: {tenantId}", 
            "Content-Type: application/json", 
            "Accept: application/json",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> dsUpdate(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId, 
            @RequestBody(required = true) final String body) throws Exception;

    @RequestLine("POST /rz45y3ihf1re/unsecured/schema/orm/delete")
    @Headers(value = { "X-Tenant-Id: {tenantId}", 
            "Content-Type: application/json", 
            "Accept: application/json",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> dsDelete(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId, 
            @RequestBody(required = true) final String body) throws Exception;

    @RequestLine("POST /0w4n6fdkjfb1q/unsecured/schema/orm/execute-transaction")
    @Headers(value = { "X-Tenant-Id: {tenantId}", 
            "Content-Type: application/json", 
            "Accept: application/json",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> dsExecuteStatements(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId, 
            @RequestBody(required = true) final String body) throws Exception;

    /*
    @RequestLine("PUT /unsecured/schema/transaction")
    @Headers(value = { "UserIDToken: {userIDToken}", "Content-Type: application/json",
            "Accept: application/json" })
    public ResponseEntity<String> transact(
            @Param("tenantId") String tenantId,
            @RequestBody(required = true) final String body) throws Exception;
     */

    @RequestLine("GET /a6tr94wpa3m70/unsecured/org/project/dataschema/entities")
    @Headers(value = { "X-Tenant-Id: {tenantId}",
            "x-b3-traceid: {id}",
            "x-b3-spanid: {spanId}",
            "x-b3-parentspanid: {parentSpanId}" })
    public ResponseEntity<String> getEntitiesDeployedToDataSchema(
            @Param("id") String traceId,
            @Param("spanId") String spanId,
            @Param("parentSpanId") String parentSpanId,
            @Param("tenantId") String tenantId) throws Exception;
}
