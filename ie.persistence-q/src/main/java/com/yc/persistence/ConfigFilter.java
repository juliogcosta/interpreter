package com.yc.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yc.error.ErrorLogger;
import com.yc.persistence.api.UnsecuredServiceController;
import com.yc.persistence.api.UnsecuredTransactionController;
import com.yc.persistence.api.client.DBRDriverRESTClient;
import com.yc.persistence.api.client.ForgerRESTClient;
import com.yc.persistence.filter.URLFilter;
import com.yc.persistence.service.QueryServiceImpl;
import com.yc.persistence.service.SchemaCacheServiceImpl;
import com.yc.utils.bean.ContextManagerControlImpl;

@Configuration
public class ConfigFilter
{
    @Value("${yc.baas.project.contract.x-tenant-id}")
    private String contractXTenantID;

    @Value("${spring.application.name}")
    public String appName;

    @Value("${yc.api.management.server.log.control.display}")
    public Boolean logDisplay;

    @Value("${yc.api.management.server.log.control.data.persistence.ttl}")
    public Long ttl;

    @Autowired
    private ForgerRESTClient forgerRESTClient;

    //@Autowired
    //private NoSQLDriverRESTClient noSQLDriverRESTClient;

    @Autowired
    private DBRDriverRESTClient sqlDriverRESTClient;

    @Autowired
    private ErrorLogger errorLogger;

    @Autowired
    private QueryServiceImpl queryService;

    @Autowired
    private SchemaCacheServiceImpl schemaCacheService;

    @Autowired
    private ContextManagerControlImpl contextManagerControl;

    @Bean
    FilterRegistrationBean<URLFilter> filterRegistrationBean()
    {
        final URLFilter urlFilter = new URLFilter();
        urlFilter.setAppName(this.appName);
        urlFilter.setLogDisplay(this.logDisplay);
        urlFilter.setTtl(this.ttl);
        urlFilter.setForgerRESTClient(this.forgerRESTClient);
        urlFilter.setSQLDriverRESTClient(this.sqlDriverRESTClient);
        urlFilter.setSchemaCacheService(this.schemaCacheService);
        urlFilter.setContextManagerControl(this.contextManagerControl);
        urlFilter.setErrorLogger(this.errorLogger);
        urlFilter.setQueryService(this.queryService);
        urlFilter.setContractXTenantID(this.contractXTenantID);

        final FilterRegistrationBean<URLFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(urlFilter);
        registrationBean.addUrlPatterns("/s/*", "/m/*", UnsecuredServiceController.urlPrefix.concat("/*"), UnsecuredTransactionController.urlPrefix.concat("/*"));
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
