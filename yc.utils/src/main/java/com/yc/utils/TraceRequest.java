package com.yc.utils;

public class TraceRequest
{
    public String tenantId;
    public String id;
    public String spanId;
    public String parentSpanId;
    public TraceRequest(String tenantId, String traceId, String spanId, String parentSpanId) {
        this.tenantId = tenantId;
        this.id = traceId;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
    }
}
