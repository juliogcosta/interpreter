package com.yc.persistence.model;

import java.util.HashSet;
import java.util.Set;

public class CUser implements br.edu.ufrn.loco3.security.User
{
    private String username;

    private Set<br.edu.ufrn.loco3.security.Role> cRoles;

    public CUser(String username, Set<br.edu.ufrn.loco3.security.Role> cRoles)
    {
        this.username = username;

        this.cRoles = cRoles;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public Set<br.edu.ufrn.loco3.security.Role> getRoles()
    {
        
        return new HashSet<>(cRoles);
    }
}
