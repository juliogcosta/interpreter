package com.yc.persistence.model;

public class CRole implements br.edu.ufrn.loco3.security.Role 
{
    private String name;

    public CRole(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }
}