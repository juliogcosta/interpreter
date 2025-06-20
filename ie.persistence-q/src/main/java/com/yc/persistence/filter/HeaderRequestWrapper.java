package com.yc.persistence.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class HeaderRequestWrapper extends HttpServletRequestWrapper
{
    /**
     * construct a wrapper for this request
     * 
     * @param request
     */
    public HeaderRequestWrapper(HttpServletRequest request) 
    {
        super(request);
    }

    private Map<String, String> headerMap = new HashMap<String, String>();

    /**
     * add a header with given name and value
     * 
     * @param name
     * @param value
     */
    public void addHeader(String name, String value)
    {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name)
    {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name))
        {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames()
    {
        List<String> names = Collections.list(super.getHeaderNames());
        for (String name : headerMap.keySet())
        {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name)
    {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name))
        {
            values.add(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

}
