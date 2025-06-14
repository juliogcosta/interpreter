package com.yc.persistence.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.yc.collector.LogCollector;
import com.yc.error.ErrorLogger;
import com.yc.persistence.service.SchemaCacheServiceImpl;
import com.yc.pr.models.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Controller extends com.yc.utils.Controller
{
    public enum Command { CREATE, READ, UPDATE, DELETE };

    @Autowired
    protected ErrorLogger errorLogger;

    @Autowired
    protected LogCollector logCollector;

    @Autowired
    protected SchemaCacheServiceImpl schemaCacheService;

    public ErrorLogger getErrorLogger()
    {
        return errorLogger;
    }

    public LogCollector getLogCollector()
    {
        return logCollector;
    }

    public void checkTenantIDAuthority(@NotNull User user, @NotNull @NotBlank String tenantId) throws Exception
    {
        //logger.info(" > tenantId: "+tenantId);
        //logger.info(" > user.getTenants(): "+user.getTenants());
        if (!user.getTenants().contains(tenantId))
        {
            throw new Exception(String.valueOf(HttpStatus.FORBIDDEN.value()).concat(":forbidden request."));
        }
    }
}
