package com.yc.collector;

public interface LogCollector
{
    void registerApplicationConsumption(String cmcKey, String tenantId, String httpEndpoint, Long length, Integer type, Integer action, Long datetime, Long latency);

    void registerPlatformConsumption(String cmcKey, String orgName, String projectName, String httpEndpoint, Long length, String type, String service, Long datetime, Long latency);
}
