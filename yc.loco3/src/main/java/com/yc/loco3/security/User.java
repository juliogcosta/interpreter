package com.yc.loco3.security;

import java.util.Set;

public interface User
{
    public String getUsername();

    public Set<Role> getRoles();
}
