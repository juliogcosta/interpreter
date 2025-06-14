package com.yc.loco3.security;

import java.util.Set;

import org.json.JSONArray;

public class UserlessAuthorizationServiceImpl implements CredentialServiceService
{
    @Override
    public void check(JSONArray authorizedRolesRorEntity) throws Exception
    {

    }

    @Override
    public String getUsername()
    {
        return null;
    }

    @Override
    public void check(JSONArray authorizedRolesRorEntity, Set<String> auths) throws Exception
    {

    }
}
