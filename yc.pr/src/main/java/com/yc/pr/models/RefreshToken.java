package com.yc.pr.models;

import java.io.Serializable;

public class RefreshToken implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5662097153710236393L;

    private Long id;

    private User user;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
