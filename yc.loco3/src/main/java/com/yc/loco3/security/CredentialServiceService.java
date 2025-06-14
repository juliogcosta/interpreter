package com.yc.loco3.security;

import java.util.Set;

import org.json.JSONArray;

public interface CredentialServiceService
{
    public void check(JSONArray authorizedRolesRorEntity) throws Exception;

    public void check(JSONArray authorizedRolesRorEntity, Set<String> auths) throws Exception;

    public String getUsername();
}