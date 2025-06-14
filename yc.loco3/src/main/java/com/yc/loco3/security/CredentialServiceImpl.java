package com.yc.loco3.security;

import java.util.Set;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CredentialServiceImpl implements CredentialServiceService
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    private User user;

    public CredentialServiceImpl(User user)
    {
        this.user = user;
    }

    @Override
    public void check(JSONArray roles) throws Exception
    {
        for (Role role : user.getRoles())
        {
            for (int idx = 0; idx < roles.length(); idx++)
            {
                if (role.getName().equals("ROLE_".concat(roles.getString(idx))))
                {
                    return ;
                }
            }
        }

        throw new Exception("403:action unauthorized.");
    }

    @Override
    public void check(JSONArray roles, Set<String> auths) throws Exception
    {
        boolean ok = true;
        for (Role role : user.getRoles())
        {
            for (int idx = 0; idx < roles.length(); idx++)
            {
                auths.add(roles.getString(idx));

                if (role.getName().equals("ROLE_".concat(roles.getString(idx))))
                {
                    ok = false;
                }
            }
        }

        if (ok) throw new Exception("403:action unauthorized.");
    }

    @Override
    public String getUsername()
    {
        return user.getUsername();
    }
}
