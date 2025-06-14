package com.yc.persistence.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Service
public class NamingHelper
{
    @Autowired
    private EurekaClient discoveryClient;

    @Value("${app.persistenceq.name}")
    private String persistenceQName;

    @Value("${app.forger.name}")
    private String forgerName;

    public String getPersistenceQDCAddress() 
    {
        InstanceInfo instance = this.discoveryClient.getNextServerFromEureka(this.persistenceQName.toUpperCase(), false);

        return instance.getIPAddr().concat(":").concat(String.valueOf(instance.getPort()));
    }

    public String getModelerAddress() 
    {
        InstanceInfo instance = this.discoveryClient.getNextServerFromEureka(this.forgerName.toUpperCase(), false);

        return instance.getIPAddr().concat(":").concat(String.valueOf(instance.getPort()));
    }
}
