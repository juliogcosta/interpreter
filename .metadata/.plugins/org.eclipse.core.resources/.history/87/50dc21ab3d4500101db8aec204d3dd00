package com.yc.collector;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.collector.dispatcher.CollectorExchangeSender;
import com.yc.utils.ContextManagerControl;

@Component
public class LogCollectorImpl implements LogCollector
{
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    public CollectorExchangeSender exchangeSender;

    @Autowired
    public ContextManagerControl contextManagerControl;

    public void registerApplicationConsumption(String cmcKey, String tenantId, String httpEndpoint, Long length, Integer type, Integer action, Long datetime, Long latency)
    {
        final JSONObject consumption = new JSONObject();
        consumption.put("tenantId", tenantId);
        consumption.put("endpointUrl", httpEndpoint);
        consumption.put("length", length);
        consumption.put("type", type);
        consumption.put("action", action);
        consumption.put("latency", latency);
        consumption.put("datetime", datetime);

        try
        {
            this.exchangeSender.registerApplicationConsumption(consumption.toString());
        }
        catch (Exception e)
        {
            if (this.contextManagerControl.containsKey(cmcKey))
            {
                if (this.contextManagerControl.getLogConsole(cmcKey))
                {
                    e.printStackTrace();
                }
            }
            else
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerPlatformConsumption(String cmcKey, String orgName, String projectName, String httpEndpoint, Long length, String type, String service, Long datetime, Long latency)
    {
        final JSONObject consumption = new JSONObject();
        consumption.put("orgName", orgName);
        consumption.put("projectName", projectName);
        consumption.put("endpointUrl", httpEndpoint);
        consumption.put("length", length);
        consumption.put("type", type);
        consumption.put("service", service);
        consumption.put("latency", latency);
        consumption.put("datetime", datetime);

        try
        {
            this.exchangeSender.registerApplicationConsumption(consumption.toString());
        }
        catch (Exception e)
        {
            if (this.contextManagerControl.containsKey(cmcKey))
            {
                if (this.contextManagerControl.getLogConsole(cmcKey))
                {
                    e.printStackTrace();
                }
            }
            else
            {
                e.printStackTrace();
            }
        }
    }
}
